import struct
import sys
import gzip
from typing import List

from seqosc.OSCSample import OSCSample


class OSCBuffer:
    def __init__(self, comment=""):
        self.speed: float = 1.0
        self.samples: List[OSCSample] = []
        self.comment = comment

    @staticmethod
    def _read_flag(flags: int, index: int) -> bool:
        return ((flags >> index) & 1) == 1

    def read(self, data: bytes):
        # todo: optimize by using unpack_from
        i = 0

        # read header
        flags, sample_count, payload_length, speed, comment_length = struct.unpack('<iiifi', data[i:i + 20])
        i += 20
        self.comment = data[i:i + comment_length].decode("utf-8")
        i += comment_length

        compressed = self._read_flag(flags, 0)
        if sample_count < 0:
            sample_count = sys.maxsize
        self.speed = speed

        # extract payload
        raw_payload = data[i:]

        # compression
        if compressed:
            raw_payload = gzip.decompress(raw_payload)

        payload = raw_payload

        # read samples
        count = 0
        p = 0
        while count < sample_count and p < len(payload):
            timestamp, length = struct.unpack('<qi', payload[p:p + 12])
            p += 12

            packet_data = payload[p:p + length]
            self.samples.append(OSCSample(timestamp, packet_data))

            p += length
            count += 1

    def write(self, compressed: bool = False) -> bytes:
        # write payload
        sample_count = len(self.samples)
        payload_length = (sample_count * (8 + 4)) + sum(len(s.packet) for s in self.samples)
        payload = bytearray(payload_length)

        i = 0
        sample_header_length = 8 + 4
        for sample in self.samples:
            length = len(sample.packet)

            struct.pack_into('<qi', payload, i, sample.timestamp, length)
            i += sample_header_length

            # write sample packet
            payload[i:i + length] = sample.packet
            i += length

        raw_payload = payload

        # compress
        if compressed:
            raw_payload = gzip.compress(raw_payload)

        # header
        raw_comment = self.comment.encode('utf-8')
        comment_length = len(raw_comment)
        header_length = (5 * 4) + comment_length

        # todo: set flag by bitmask
        flags = int(compressed)

        # set data
        data = bytearray(header_length + len(raw_payload))
        struct.pack_into('<iiifi', data, 0, flags, len(self.samples), payload_length, self.speed, comment_length)
        data[header_length - comment_length:header_length] = raw_comment
        data[header_length:] = raw_payload

        return data

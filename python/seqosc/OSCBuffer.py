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
        while count < sample_count and i < len(payload):
            timestamp, length = struct.unpack('<qi', payload[p:p + 12])
            p += 12

            packet_data = payload[p:p + length]
            self.samples.append(OSCSample(timestamp, packet_data))

            p += length
            count += 1

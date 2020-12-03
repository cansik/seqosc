class OSCSample:

    def __init__(self, timestamp, packet):
        self.timestamp: int = timestamp
        self.packet: bytes = packet

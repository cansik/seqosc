from seqosc.OSCBuffer import OSCBuffer


def get_bytes_from_file(filename: str) -> bytes:
    with open(filename, "rb") as file:
        return file.read()


def write_bytes_from_file(filename: str, data: bytes):
    with open(filename, "wb") as file:
        return file.write(data)


def main():
    print("reading buffer...")

    buffer = OSCBuffer()
    buffer.read(get_bytes_from_file("florian.osc"))

    print("Samples: %d" % len(buffer.samples))
    print("writing...")

    write_bytes_from_file("test.osc", buffer.write(compressed=True))

    b = OSCBuffer()
    b.read(get_bytes_from_file("test.osc"))

    print("Samples 2: %d" % len(b.samples))


if __name__ == "__main__":
    main()

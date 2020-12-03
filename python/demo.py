from seqosc.OSCBuffer import OSCBuffer


def get_bytes_from_file(filename):
    return open(filename, "rb").read()


def main():
    print("reading buffer...")

    buffer = OSCBuffer()
    buffer.read(get_bytes_from_file("florian.osc"))

    print("Samples: %d" % len(buffer.samples))


if __name__ == "__main__":
    main()

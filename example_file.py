import msgpack
import requests

import pandas

f = open("/tmp/msgpack", 'rb')
unpacker = msgpack.Unpacker(f)
header = next(unpacker)
df = pandas.DataFrame((x for x in unpacker), columns=header)
f.close()

print(df)
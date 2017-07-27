import msgpack
import requests

import pandas

r = requests.get("http://localhost:8080/msgpack", stream=True)
unpacker = msgpack.Unpacker(r.raw)
header = next(unpacker)
df = pandas.DataFrame((x for x in unpacker), columns=header)
r.close()

print(df)
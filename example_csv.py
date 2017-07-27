import requests

import pandas

r = requests.get("http://localhost:8080/tsv", stream=True)

df = pandas. pandas.read_table(r.raw)
r.close()

print(df)
import io
import base64
import requests

def main(data):
    image = io.BytesIO(base64.b64decode(data))

    r = requests.post(
        "https://api.deepai.org/api/colorizer",
        files={
            'image': image,
        },
        headers={'api-key': 'b3a13115-c082-4fb5-9823-956292880789'}
    )

    dic = r.json()
    url = dic['output_url']
    response = requests.get(url)
    res_img = io.BytesIO(response.content)
    img_str = base64.b64encode(res_img.getvalue())
    return ""+str(img_str,'utf-8')
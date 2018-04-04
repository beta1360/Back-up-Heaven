'''
Copyright (c) The end of Computer Engineering
            : Keon-hee Lee, Dae-kyo Jeong, Ju-mi Ryu, Hye-won Choi
LICENSE : BSD 3-clause License

- Description : https://github.com/KeonHeeLee/Back-up-Heaven
- If you want to contact us, please send mail "beta1360@naver.com"
'''

#-*-coding: utf-8-*-

from flask import Flask,request,jsonify
from pymongo import MongoClient
import json

app = Flask(__name__)

@app.route("/test",methods=["POST"])
def test():
    req = request.get_json()
    if req["user"] == "tester":
        print(req["text"])

    conn = MongoClient("localhost",27017)
    db = conn.test
    collection = db.test

    data = collection.find_one()

    test_dict = {"test" : data["test"] }
    conn.close()

    return jsonify(test_dict), 200


if __name__ == "__main__":
    ssl_cert = '/etc/letsencrypt/live/daeta.ga/fullchain.pem'
    ssl_key =  '/etc/letsencrypt/live/daeta.ga/privkey.pem'
    contextSSL =  (ssl_cert, ssl_key)
    app.run(host='0.0.0.0', port=443, debug = True, ssl_context = contextSSL)

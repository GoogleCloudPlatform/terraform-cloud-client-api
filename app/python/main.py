from flask import Flask


app = Flask(__name__)


@app.route("/")
def home():
    content = {}
    return render_template("index.html", content=content)


if __name__ == "__main__":
    app.run(host="localhost", port=8080, debug=True)
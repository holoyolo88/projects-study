import RPi.GPIO as GPIO
from flask import Flask, render_template

app = Flask(__name__)

GPIO.setmode(GPIO.BCM)
GPIO.setup(21, GPIO.OUT)

def led_on():
	GPIO.output(21, 0)
	return
def led_off():
	GPIO.output(21, 1)
	return
@app.route('/')
def index():
	led_off()
	return render_template('index.html', state='OFF')
	
@app.route('/on/')
def on():
	led_on()
	print ('Switch On')
	return render_template('index.html', state='ON')
@app.route('/off/')
def off():
	led_off()
	print ('Switch Off')
	return render_template('index.html', state='OFF')
		
if __name__=='__main__':
	print('Web Server Starts')
	app.run(debug=True, host='172.30.2.115', port=8000)
	print ('Web Server Ends')

GPIO.cleanup()
Print('Program Ends')

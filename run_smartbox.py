import pyrebase
import time
import RPi.GPIO as GPIO
import os
import serial
import zmail
import datetime


email=os.environ['EMAIL']
passemail=os.environ['PASSEMAIL']
msg= 'Human found at  ' + datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")

mail = {
    'subject': 'SMART POSTBOX!',  # Anything you want.
    'content_text': msg,  
    'attachments': ['/home/pi/tensorflow1/models/research/object_detection/human.jpg'],  
}
server = zmail.server(email,passemail)
#xbee setup
ser = serial.Serial(
    port='/dev/ttyAMA0',
    baudrate = 9600,
    parity=serial.PARITY_NONE,
    stopbits=serial.STOPBITS_ONE,
    bytesize=serial.EIGHTBITS,
    timeout=1
 )

def xbee(no):
    i=1
    while i < 4:
       ser.write(str(no).encode('ascii'))
       time.sleep(1)
       i+=1
    

def relay(d):
    GPIO.setmode(GPIO.BCM) 
    GPIO.setup(d, GPIO.OUT)
    GPIO.output(d,GPIO.LOW)#door lock
    time.sleep(5)
    GPIO.cleanup()
    


config = {
    "apiKey": "xxxxxxxxxxxxxxxxxx",
    "authDomain": "xxxxxxxxxxx.firebaseapp.com",
    "databaseURL": "https://xxxxxxxxxxxf.firebaseio.com",
    "projectId": "xxxxxxxxxxxxxx",
    "storageBucket": "xxxxxxxxxxxx.appspot.com",
    "messagingSenderId": "xxxxxxxxxxxxx5",
    "appId": "1:xxxxxxxxxxx:web:6xxxxxxxxx33468c",
    "measurementId": "G-Fxxxxxxxxxxx"
  }

firebase = pyrebase.initialize_app(config)
db = firebase.database()
time.sleep(10)
db.child("box").update({"button":"off"})

while True:
     GPIO.setmode(GPIO.BCM)
     GPIO.setup(20, GPIO.IN, pull_up_down=GPIO.PUD_UP)
     input_state=GPIO.input(20)
     
     if input_state == False:
            print('button on')
            print('take a pic')
            os.system('sudo fswebcam -r 640x480 human.jpg')
            print('sending mail')
            server.send_mail('xxxxxxxxxxxx@gmail.com', mail)
            db.child("box").update({"button":"on"})
            time.sleep(5)
            db.child("box").update({"button":"off"})
            i=100
            while(i>0):
                  print("waiting for owner to open the box "+str(i)+"left")
                  value1 =db.child("box").child("door").get()
                  doorinfb1=value1.val()
                  if doorinfb1=='open' :
                         print("opening door")
                         relay(23)
                         db.child("box").update({"door":"close"})
                         print("door closed")
                         time.sleep(5)
                         #on light
                         GPIO.setmode(GPIO.BCM)
                         GPIO.setup(22, GPIO.OUT)
                         GPIO.output(22,GPIO.LOW)
                         #classifiaction
                         os.system('python3 smartbox.py')
                         #get the type of object
                         file=open("test.txt","r")
                         x=file.read()
                         print (x)
                         #send to xbee
                         xbee(x)
                         #off light
                         GPIO.cleanup()
                         i=0
                  i-=1
     else :
            print('button off')
     
     value =db.child("box").child("door").get()
     doorinfb=value.val()
     
     if doorinfb=='open' :
             print("opening door")
             relay(23)
             db.child("box").update({"door":"close"})
             print("door closed")
             time.sleep(5)


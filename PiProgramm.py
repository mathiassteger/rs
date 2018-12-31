import bluetooth
from Queue import Queue
from threading import Thread
import time
import os
import json
import RPi.GPIO as GPIO

server_socket=bluetooth.BluetoothSocket( bluetooth.RFCOMM )

port = 1
server_socket.bind(("",port))
server_socket.listen(1)

def initPins():
  GPIO.setmode(GPIO.BOARD) # Access GPIO pins by pin number not GPIO number
  GPOI.setup(8, GPIO.OUT) # Set GPIO pin to output
  GPOI.setup(10, GPIO.OUT) # Set GPIO pin to output
  GPOI.setup(12, GPIO.OUT) # Set GPIO pin to output
  GPOI.setup(14, GPIO.OUT) # Set GPIO pin to output
  GPOI.setup(16, GPIO.OUT) # Set GPIO pin to output
  GPOI.setup(18, GPIO.OUT) # Set GPIO pin to output
  GPOI.setup(22, GPIO.OUT) # Set GPIO pin to output
  GPOI.setup(24, GPIO.OUT) # Set GPIO pin to output
  GPOI.setup(26, GPIO.OUT) # Set GPIO pin to output
  GPOI.setup(28, GPIO.OUT) # Set GPIO pin to output
  GPOI.setup(32, GPIO.OUT) # Set GPIO pin to output

def pin(pinNumber, delay): #Thread to start rocket with pin number (call with "Thread(target=pin, args=($pinNmbr$,$delay$,)).start()")
 time.sleep(delay)
 GPIO.output(pinNumber, 1) # HIGH
 time.sleep(1) #sleep for number of seconds the pin should be on
 GPIO.output(pinNumber, 0) # HIGH
 #TODO: release pin

def worker(q): #Consumer Thread
 while 1:
  print "worker running"
  if(not q.empty()):
   print "Consumer consumed: ", q.get()

def init(q): #Producer (main) Thread
 print "initiating Bluetooth socket"
 try:
  client_socket,address = server_socket.accept()
  print "Accepted connection from ",address
  while 1:

   data = client_socket.recv(1024)
   print "Received: %s" % data
   try:
    data = json.loads(data)
    for p in data["pins"]:
      Thread(target=pin, args=(p,data["delay"],)).start()
   except ValueError:
    print "Recevied Data not in json format"
   q.put(data)
   if data == "q":
    print ("Quit")
    break

  client_socket.close()
  server_socket.close()

 except bluetooth.btcommon.BluetoothError:
  print "Current connection Closed"
  init(q)

if __name__ == "__main__":
 try:
  q = Queue()
  worker = Thread(target=worker, args=(q,))
  worker.setDaemon(True)
  worker.start()
  init(q)
 except KeyboardInterrupt:
  print "Keyboard Interrupt, ending Process"
  #TODO: release all Pins
  os._exit(0)
import bluetooth
from Queue import Queue
from threading import Thread
import time
import os
import json

server_socket=bluetooth.BluetoothSocket( bluetooth.RFCOMM )

port = 1
server_socket.bind(("",port))
server_socket.listen(1)

def pin(pinNumber, delay): #Thread to start rocket with pin number (call with "Thread(target=pin, args=($pinNmbr$,$delay$,)).start()")
 time.sleep(delay)
 print "setting pin: ", pinNumber #TODO: implement function
 time.sleep(1) #sleep for number of seconds the pin should be on
 print "setting pin off: ", pinNumber #TODO: implement function

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
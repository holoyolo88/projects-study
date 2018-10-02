from PyQt5 import QtCore, QtGui, QtWidgets
from PyQt5.QtGui import *
import serial
from source_ui import *
import threading
import sys
from time import sleep

ser = serial.Serial(port='COM4',
    				baudrate=115200,
        			parity=serial.PARITY_NONE,
        			stopbits=serial.STOPBITS_ONE,
        			bytesize=serial.EIGHTBITS,
    				timeout=1)

# 인코딩 설정 변경하기
def receiveData(self, ui):
	while True:      
		rxdata = ser.readline().decode('utf-8')
		if rxdata :
			print(rxdata)
            

def sendData(self):
    pushButton_list =[self.pushButton1, self.pushButton2, self.pushButton3,
    self.pushButton4, self.pushButton5, self.pushButton6, self.pushButton7,
    self.pushButton8, self.pushButton9, self.pushButton10]
    for pushButton in pushButton_list:
        if pushButton.isChecked():
            ID=str(int(pushButton.text())+100)[1:]
            W=str(int(self.W.text())+1000)[1:]
            R=str(int(self.R.text())+1000)[1:]
            G=str(int(self.G.text())+1000)[1:]
            B=str(int(self.B.text())+1000)[1:]
            message = ''.join(['\x02','L',ID,'W',W,'R',R,'G',G,'B',B,'\x0d\x0a\x03'])
            ser.write(bytes(message.encode()))
            print(message)
            sleep(0.4)


stylesheet_1 = 'QPushButton{border: 2px solid rgba(255, 180, 0);border-radius:5px;}' \
            +'QPushButton:checked{border: 2px solid rgba(255, 180, 0);' \
            +'background-color:qlineargradient(spread:reflect, x1:0.483889, y1:0, x2:0.500333, y2:0.512,' \
            +'stop:0 rgba(255, 180, 0, 255), stop:1 rgba(255, 229, 44, 255));' \
            +'border-radius:5px;}'

stylesheet_2 = 'QPushButton{border: 2px solid rgba(255, 180, 0);border-radius:5px;}' \
            +'QPushButton:pressed{border: 2px solid rgba(255, 180, 0);' \
            +'background-color:qlineargradient(spread:reflect, x1:0.483889, y1:0, x2:0.500333, y2:0.512,' \
            +'stop:0 rgba(255, 180, 0, 255), stop:1 rgba(255, 229, 44, 255));' \
            +'border-radius:5px;}'


def signals(self):
    pushButton_list =[self.pushButton1, self.pushButton2, self.pushButton3,
    self.pushButton4, self.pushButton5, self.pushButton6, self.pushButton7,
    self.pushButton8, self.pushButton9, self.pushButton10]
    for pushButton in pushButton_list:
        pushButton.setStyleSheet(stylesheet_1)
    self.SEND.setStyleSheet(stylesheet_2)
    self.EXIT.setStyleSheet(stylesheet_2)
    self.SEND.clicked.connect(self.sendData)
    self.EXIT.clicked.connect(lambda:sys.exit(app.exec_()))

Ui_MainWindow.signals = signals
Ui_MainWindow.sendData = sendData

if __name__=='__main__':
    app = QtWidgets.QApplication(sys.argv)
    MainWindow = QtWidgets.QMainWindow()
    ui = Ui_MainWindow()
    ui.setupUi(MainWindow)
    ui.signals()
    thread = threading.Thread(target=receiveData, args=(ser,ui))
    thread.daemon = True
    thread.start()
    MainWindow.show()
    sys.exit(app.exec_())

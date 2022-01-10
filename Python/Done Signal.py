#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Jan  7 13:54:29 2022
@author: rajaabengad
"""

import matplotlib.pyplot as plt
import numpy as np
from scipy import signal
import wfdb


fs=500
f1=50
f0=0.6
bw=5
q0=f0/bw
q1=f1/bw

Ts = 1/fs
 
t = np.arange(0,2000*Ts,Ts)


# Indlæs EKG signalet (råt, ufiltreret) samt tilhørende info
signals, info = wfdb.rdsamp('rec_1', channels=[0, 1], 
                              sampfrom=0, sampto=2000)

# 0 er råt signal, 1 er filtreret signal
ecg0 = signals[:,0]
ecg1 = signals[:,1]

# Plot signal i tidsdomæne
plt.plot(t,ecg1,color='black')
plt.title('Tidsdomæne')
plt.xlabel('tid (s)')
plt.ylabel('mV')
plt.show()

# Plot signal i frekvensdomæne
plt.magnitude_spectrum(ecg1,Fs=500,color='black')
plt.title('frekvensdomæne')
plt.show()

#notch filter til at fjerne baseline wander

b, a = signal.iirnotch(f0, q0, fs)
freqz0,h0 = signal.freqz(b, a, fs=fs)

outputSignal = signal.filtfilt(b, a, ecg0)

#notch filter til at fjerne powerline 

c, d = signal.iirnotch(f1, q1, fs)
freqz1,h1= signal.freqz(c, d, fs=fs)

outputSignal2 = signal.filtfilt(c, d, outputSignal)



#MA filter til at fjerne EMG noise 
x=np.array([1/8,1/8,1/8,1/8,1/8,1/8,1/8,1/8])

filtret = signal.filtfilt(x, [1], outputSignal)

#plotte filtre sort ecg1 blå=vores filtret signal 
plt.plot(t,filtret)
plt.ylim([-0.2,0.6])
plt.plot(t,ecg1,color='black')
plt.title('Tidsdomæne')
plt.xlabel('tid (s)')
plt.ylabel('mV')
plt.show()


#plotte filtre sort ecg1 blå=vores filtret signal 
plt.plot(t,filtret)
plt.ylim([-0.2,0.6])
plt.show()


# plt.plot(t,filtret )
# plt.ylim([-0.2,0.6])
# plt.plot(t,ecg0,color='red')
# plt.title('Tidsdomæne')
# plt.xlabel('tid (s)')
# plt.ylabel('mV')
# plt.show()

plt.magnitude_spectrum(filtret,Fs=500)
plt.title('frekvensdomæne')
plt.show()
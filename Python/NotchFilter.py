#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Thu Jan  6 14:08:12 2022
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
plt.plot(t,ecg0)
plt.title('Tidsdomæne')
plt.xlabel('tid (s)')
plt.show()

# Plot signal i frekvensdomæne
plt.magnitude_spectrum(ecg0,Fs=500)
plt.show()


#notch filter
#blå= rå ekg rød= ekg gennem notch filter 

b, a = signal.iirnotch(f0, q0, fs)
freqz0,h0 = signal.freqz(b, a, fs=fs)

outputSignal = signal.filtfilt(b, a, ecg0)

plt.plot(outputSignal, color='green')
plt.plot(ecg0, color='blue')
plt.show()

c, d = signal.iirnotch(f1, q1, fs)
freqz1,h1= signal.freqz(c, d, fs=fs)

outputSignal2 = signal.filtfilt(c, d, outputSignal)

plt.plot(outputSignal2, color='red')
plt.plot(ecg0, color='blue')
plt.show()



plt.subplot(211)
plt.plot(outputSignal, color='green')
plt.title('baseline fjernet')
plt.subplot(212)
plt.plot(outputSignal2, color='red')
plt.title('powerline fjernet')
plt.show()

plt.subplot(211)
plt.magnitude_spectrum(outputSignal, Fs=500, color='green')
plt.ylim([0,0.04])

plt.subplot(212)
plt.magnitude_spectrum(outputSignal2,Fs=500,color='red')
plt.ylim([0,0.04])
plt.show()

plt.magnitude_spectrum(ecg0,Fs=500)
plt.title('Med støj i spektret')
plt.show()
plt.magnitude_spectrum(outputSignal2,Fs=500)
plt.ylim([0,0.04])
plt.title('Filteret i spektret')
plt.show()
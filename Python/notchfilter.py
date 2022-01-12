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
q1=f1/120


Ts = 1/fs
t = np.arange(0,10000*Ts,Ts)

# Indlæs EKG signalet (råt) samt tilhørende info
signals, info = wfdb.rdsamp('rec_5', channels=[0, 1], 
                              sampfrom=0, sampto=10000)

# 0 er råt signal, 1 er filtreret signal
ecg0 = signals[:,0]
ecg1 = signals[:,1]

# Plot signal i tidsdomænet
plt.plot(t,ecg0)
plt.title('Tidsdomæne')
plt.xlabel('tid (s)')
plt.ylabel('Amplitude (mV)')
plt.show()

# Plot signal i frekvensdomæne
plt.magnitude_spectrum(ecg0,Fs=500)
plt.show()


#notch filter
#blå= rå ekg rød= ekg gennem notch filter 
b, a = signal.iirnotch(f0, q0, fs)
freqz0,h0 = signal.freqz(b, a, fs=fs)

outputSignal = signal.filtfilt(b, a, ecg0)

plt.plot(t, outputSignal, color='green')
plt.plot(t, ecg0, color='blue')
plt.xlabel('tid (s)')
plt.ylabel('Amplitude (mV)')
plt.show()

c, d = signal.iirnotch(f1, q1, fs)
freqz1,h1= signal.freqz(c, d, fs=fs)

outputSignal2 = signal.filtfilt(c, d, outputSignal)

plt.plot(t, outputSignal2, color='red')
plt.plot(t, ecg0, color='blue')
plt.xlabel('tid (s)')
plt.ylabel('Amplitude (mV)')
plt.show()


plt.subplot(211)
plt.plot(t, outputSignal, color='green')
plt.title('Baseline fjernet')
plt.xlabel('tid (s)')
plt.ylabel('Amplitude (mV)')

plt.subplot(212)
plt.plot(t, outputSignal2, color='red')
plt.title('Powerline fjernet')
plt.xlabel('tid (s)')
plt.ylabel('Amplitude (mV)')
plt.tight_layout()
plt.show()

plt.subplot(211)
plt.magnitude_spectrum(outputSignal, Fs=500, color='green')
plt.ylim([0,0.04])

plt.subplot(212)
plt.magnitude_spectrum(outputSignal2,Fs=500,color='red')
plt.ylim([0,0.04])
plt.tight_layout()
plt.show()

plt.magnitude_spectrum(ecg0,Fs=500)
plt.title('Med støj i spektret')
plt.show()
plt.magnitude_spectrum(outputSignal2,Fs=500,color='red')
plt.ylim([0,0.04])
plt.title('Filteret i spektret')
plt.show()
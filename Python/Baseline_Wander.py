#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Jan  5 12:57:08 2022

@author: rajaabengad
"""


import matplotlib.pyplot as plt
import numpy as np
from scipy import signal
import wfdb


fs=500

 
Ts = 1/fs
 
t = np.arange(0,10000*Ts,Ts)

# Indlæs EKG signalet (råt, ufiltreret) samt tilhørende info
signals, info = wfdb.rdsamp('rec_5', channels=[0, 1], 
                              sampfrom=0, sampto=10000)

# 0 er råt signal, 1 er filtreret signal
ecg0 = signals[:,0]
ecg1 = signals[:,1]

# Plot signal i tidsdomæne
plt.plot(t,ecg0)
plt.title('Rå signal i tidsdomænet')
plt.xlabel('tid (s)')
plt.ylabel('Amplitude (mV)')
plt.show()

# Plot signal i frekvensdomæne
plt.magnitude_spectrum(ecg0,Fs=500)
plt.show()

#  Lavpas filter
# #FIR, highpass, zero phase, forward-backward filtrering, cutoff=0.5Hz
filter = signal.firwin(numtaps=301, fs=500, cutoff=0.6, pass_zero=False)


filtreretSignal = signal.filtfilt(filter,1,ecg0)



plt.plot(t,filtreretSignal, color='orange')




#plt.plot(t,ecg0, color='blue')
plt.xlabel('tid (s)')
plt.ylabel('Amplitude (mV)')
plt.show()

#blå= rå ekg orange= ekg gennem highpass filter 


# Filtreret signal
plt.magnitude_spectrum(filtreretSignal,Fs=500, color='orange')
plt.ylim([0,0.04])

# Ufiltreret signal
#plt.magnitude_spectrum(ecg0,Fs=500, color='green')

plt.show()
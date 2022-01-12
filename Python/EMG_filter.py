#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Jan  7 11:20:30 2022

@author: rajaabengad
"""


import matplotlib.pyplot as plt
import numpy as np
from scipy import signal
import wfdb
from pandas import Series


fs=500

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
plt.ylabel('mV')
plt.show()

# Plot signal i frekvensdomæne
plt.magnitude_spectrum(ecg0,Fs=500)
plt.title('frekvensdomæne')
plt.show()

x=np.array([1/8,1/8,1/8,1/8,1/8,1/8,1/8,1/8])


outputSignal = signal.filtfilt(x, [1], ecg0)

plt.plot(t,outputSignal)
plt.title('Tidsdomæne')
plt.xlabel('tid (s)')
plt.ylabel('mV')
plt.show()


plt.magnitude_spectrum(outputSignal,Fs=500)
plt.title('frekvensdomæne')
plt.show()
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
import Python_Post_request as PPR
import BaslineWanderImplementering as baslin


fs=500
f1=50
f0=0.6
bw=5
q0=f0/bw
q1=f1/120

Ts = 1/fs
 
sampletop= 10000
t = np.arange(0,sampletop*Ts,Ts)

rec ='rec_19'
cpr = "7777777777"

# Indlæs EKG signalet (råt, ufiltreret) samt tilhørende info
signals, info = wfdb.rdsamp(rec, channels=[0, 1], 
                              sampfrom=0, sampto=sampletop)

# 0 er råt signal, 1 er filtreret signal
ecg0 = signals[:,0]
ecg1 = signals[:,1]

#baselineWander 
baseline = baslin.baselinefilter(rec,sampletop)


#notch filter til at fjerne baseline wander

b, a = signal.iirnotch(f0, q0, fs)
freqz0,h0 = signal.freqz(b, a, fs=fs)

outputSignal = signal.filtfilt(b, a, baseline)



#notch filter til at fjerne powerline 

c, d = signal.iirnotch(f1, q1, fs)
freqz1,h1= signal.freqz(c, d, fs=fs)

outputSignal2 = signal.filtfilt(c, d, baseline)


#MA filter til at fjerne EMG noise 
x=np.array([1/8,1/8,1/8,1/8,1/8,1/8,1/8,1/8])

#filtret = signal.filtfilt(x, [1], baseline)

filtret2 = signal.filtfilt(x, [1], outputSignal2)

#filteret3= signal.filtfilt(x, [1], outputSignal2)

#plotte filtre sort ecg1 blå=vores filtret signal 
plt.subplot(211)
plt.plot(t,ecg1)
#plt.subplot(212)
#plt.plot(t,filtret)
plt.subplot(212)
plt.plot(t,filtret2)
plt.ylim([-0.2,0.6])
#plt.subplot(414)
#plt.plot(t,filteret3)
plt.ylim([-0.2,0.6])
plt.show()


PPR.postFiltEKG(cpr, filtret2.tolist())
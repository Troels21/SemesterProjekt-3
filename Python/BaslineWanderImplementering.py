# -*- coding: utf-8 -*-
"""
Created on Wed Jan 12 11:46:28 2022

@author: Spille
"""

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

def baselinefilter(rec, sampletop):

    fs=500
    
     
    Ts = 1/fs
     
    t = np.arange(0,sampletop*Ts,Ts)
    
    # Indlæs EKG signalet (råt, ufiltreret) samt tilhørende info
    signals, info = wfdb.rdsamp(rec, channels=[0, 1], 
                                  sampfrom=0, sampto=sampletop)
    
    # 0 er råt signal, 1 er filtreret signal
    ecg0 = signals[:,0]
    ecg1 = signals[:,1]
    
    
    
    #  Lavpas filter
    # #FIR, highpass, zero phase, forward-backward filtrering, cutoff=0.5Hz
    filter = signal.firwin(numtaps=301, fs=500, cutoff=1.3, pass_zero=False)
    
    
    filtreretSignal = signal.filtfilt(filter,1,ecg0)
    return filtreretSignal
    

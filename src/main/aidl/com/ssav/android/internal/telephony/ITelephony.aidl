package com.ssav.android.internal.telephony;

  interface ITelephony {
    boolean endCall();
    void answerRingingCall();
    void silenceRinger();
  }
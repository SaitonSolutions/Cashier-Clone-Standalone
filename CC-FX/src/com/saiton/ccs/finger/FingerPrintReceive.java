package com.saiton.ccs.finger;

import java.awt.image.BufferedImage;

/**
 *
 * @author Bhathiya
 */
public interface FingerPrintReceive {

    void setImage(BufferedImage img);

    void writeLog(String str);

    void fingerPrintExceptionOccured(Exception ex);
}

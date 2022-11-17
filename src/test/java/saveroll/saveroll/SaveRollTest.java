package saveroll.saveroll;

import saveroll.logging.JULHandler;
import saveroll.logging.Logger;

class SaveRollTest {

    {
        Logger.init(new JULHandler(java.util.logging.Logger.getAnonymousLogger()));
    }


}
package saveroll.saveroll.config;

import saveroll.saveroll.config.params.CalcParam;

import java.util.ArrayList;

public class Roll {
    private String placeholder;
    private ArrayList<CalcParam> params;

    public Roll(String placeholder, ArrayList<CalcParam> params) {
        this.placeholder = placeholder;
        this.params = params;
    }

    public Roll(String placeholder) {
        this(placeholder, new ArrayList<>());
    }
}

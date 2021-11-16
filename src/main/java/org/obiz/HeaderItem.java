package org.obiz;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class HeaderItem {
    private String naame;
    private String value;

    public HeaderItem(String naame, String value) {
        this.naame = naame;
        this.value = value;
    }

    public String getNaame() {
        return naame;
    }

    public void setNaame(String naame) {
        this.naame = naame;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

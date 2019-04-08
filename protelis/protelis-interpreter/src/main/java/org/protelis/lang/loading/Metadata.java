package org.protelis.lang.loading;

import java.io.Serializable;

public interface Metadata extends Serializable {

    int getStartLine();

    int getEndLine();

}

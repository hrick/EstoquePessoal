package br.com.hrick.estoquepessoal.exceptions;

import java.sql.SQLException;

/**
 * Created by Meg on 13/09/2017.
 */

public class SqlExceptionCustom extends SQLException {
    public SqlExceptionCustom(String reason) {
        super(reason);
    }
}

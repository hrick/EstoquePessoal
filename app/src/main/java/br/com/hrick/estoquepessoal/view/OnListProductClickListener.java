package br.com.hrick.estoquepessoal.view;

import android.Manifest;

import br.com.hrick.estoquepessoal.entity.Product;
import permissions.dispatcher.NeedsPermission;

/**
 * Created by Meg on 15/09/2017.
 */

public interface OnListProductClickListener {

    void onListProductClickListener(Product product);
    void onDeleteProductListener(Product product);
    @NeedsPermission(Manifest.permission.CALL_PHONE)
    void onListCallResponsibleClickListener(Product product);
}

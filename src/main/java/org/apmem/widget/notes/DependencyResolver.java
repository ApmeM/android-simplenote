package org.apmem.widget.notes;

import android.content.Context;
import org.apmem.widget.notes.datastore.RepositoryFactory;
import org.apmem.widget.notes.datastore.repositoryFactory.RepositoryFactoryFake;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 08.10.11
 * Time: 23:37
 * To change this template use File | Settings | File Templates.
 */
public class DependencyResolver {
    private static RepositoryFactory currentRepositoryFactory = null;

    public static RepositoryFactory getCurrentRepositoryFactory(Context context) {
        if (currentRepositoryFactory == null)
            currentRepositoryFactory = new RepositoryFactoryFake();

        return currentRepositoryFactory;
    }
}

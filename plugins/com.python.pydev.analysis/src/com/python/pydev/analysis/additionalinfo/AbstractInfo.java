/**
 * Copyright (c) 2005-2012 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Eclipse Public License (EPL).
 * Please see the license.txt included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
/*
 * Created on 07/09/2005
 */
package com.python.pydev.analysis.additionalinfo;

import java.io.Serializable;

import org.python.pydev.core.IPythonNature;
import org.python.pydev.core.ObjectsInternPool;

public abstract class AbstractInfo implements IInfo, Serializable {
    /**
     * Changed for 2.1
     */
    private static final long serialVersionUID = 3L;

    /**
     * the name
     */
    public final String name;

    /**
     * This is the path (may be null)
     */
    public final String path;

    /**
     * the name of the module where this function is declared
     */
    public final String moduleDeclared;

    public final IPythonNature nature;

    public AbstractInfo(String name, String moduleDeclared, String path, IPythonNature nature) {
        synchronized (ObjectsInternPool.lock) {
            this.name = ObjectsInternPool.internUnsynched(name);
            this.moduleDeclared = ObjectsInternPool.internUnsynched(moduleDeclared);
            this.path = ObjectsInternPool.internUnsynched(path);
        }
        this.nature = nature;
    }

    /**
     * Same as the other constructor but does not intern anything.
     */
    public AbstractInfo(String name, String moduleDeclared, String path, boolean doNotInternOnThisContstruct,
            IPythonNature nature) {
        this.name = name;
        this.moduleDeclared = moduleDeclared;
        this.path = path;
        this.nature = nature;
    }

    @Override
    public IPythonNature getNature() {
        return nature;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDeclaringModuleName() {
        return moduleDeclared;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IInfo)) {
            return false;
        }
        IInfo otherInfo = (IInfo) obj;

        if (otherInfo.getType() != getType()) {
            return false;
        }

        if (!otherInfo.getDeclaringModuleName().equals(this.moduleDeclared)) {
            return false;
        }

        if (!otherInfo.getName().equals(this.name)) {
            return false;
        }

        //if one of them is null, the other must also be null...
        String otherPath = otherInfo.getPath();
        String myPath = getPath();
        if ((otherPath == null || myPath == null)) {
            if (otherPath != myPath) {
                //one of them is not null
                return false;
            }
            //both are null
            return true;
        }

        //they're not null
        if (!otherPath.equals(myPath)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime*result + this.name.hashCode();
        result = prime*result + this.moduleDeclared.hashCode();
        result = prime*result + (this.path == null ? 0 : this.path.hashCode());
        result = prime*result + getType();
        return result;
    }

    @Override
    public String toString() {
        return this.name + " (" + this.moduleDeclared + ") - Path:" + getPath();
    }

    @Override
    public int compareTo(IInfo o) {
        int r = name.compareTo(o.getName());
        if (r != 0) {
            return r;
        }
        return moduleDeclared.compareTo(o.getDeclaringModuleName());
    }
}

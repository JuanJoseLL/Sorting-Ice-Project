//
// Copyright (c) ZeroC, Inc. All rights reserved.
//
//
// Ice version 3.7.9
//
// <auto-generated>
//
// Generated from file `Sorter.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package Demo;

public interface MetadataServer extends com.zeroc.Ice.Object
{
    String getStorageNodeForBlock(long blockId, com.zeroc.Ice.Current current);

    java.util.List<java.lang.Long> getAvailableBlockIds(com.zeroc.Ice.Current current);

    /** @hidden */
    static final String[] _iceIds =
    {
        "::Demo::MetadataServer",
        "::Ice::Object"
    };

    @Override
    default String[] ice_ids(com.zeroc.Ice.Current current)
    {
        return _iceIds;
    }

    @Override
    default String ice_id(com.zeroc.Ice.Current current)
    {
        return ice_staticId();
    }

    static String ice_staticId()
    {
        return "::Demo::MetadataServer";
    }

    /**
     * @hidden
     * @param obj -
     * @param inS -
     * @param current -
     * @return -
    **/
    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_getStorageNodeForBlock(MetadataServer obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        long iceP_blockId;
        iceP_blockId = istr.readLong();
        inS.endReadParams();
        String ret = obj.getStorageNodeForBlock(iceP_blockId, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeString(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    /**
     * @hidden
     * @param obj -
     * @param inS -
     * @param current -
     * @return -
    **/
    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_getAvailableBlockIds(MetadataServer obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        inS.readEmptyParams();
        java.util.List<java.lang.Long> ret = obj.getAvailableBlockIds(current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        if(ret == null)
        {
            ostr.writeSize(0);
        }
        else
        {
            ostr.writeSize(ret.size());
            for(long elem : ret)
            {
                ostr.writeLong(elem);
            }
        }
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    /** @hidden */
    final static String[] _iceOps =
    {
        "getAvailableBlockIds",
        "getStorageNodeForBlock",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping"
    };

    /** @hidden */
    @Override
    default java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceDispatch(com.zeroc.IceInternal.Incoming in, com.zeroc.Ice.Current current)
        throws com.zeroc.Ice.UserException
    {
        int pos = java.util.Arrays.binarySearch(_iceOps, current.operation);
        if(pos < 0)
        {
            throw new com.zeroc.Ice.OperationNotExistException(current.id, current.facet, current.operation);
        }

        switch(pos)
        {
            case 0:
            {
                return _iceD_getAvailableBlockIds(this, in, current);
            }
            case 1:
            {
                return _iceD_getStorageNodeForBlock(this, in, current);
            }
            case 2:
            {
                return com.zeroc.Ice.Object._iceD_ice_id(this, in, current);
            }
            case 3:
            {
                return com.zeroc.Ice.Object._iceD_ice_ids(this, in, current);
            }
            case 4:
            {
                return com.zeroc.Ice.Object._iceD_ice_isA(this, in, current);
            }
            case 5:
            {
                return com.zeroc.Ice.Object._iceD_ice_ping(this, in, current);
            }
        }

        assert(false);
        throw new com.zeroc.Ice.OperationNotExistException(current.id, current.facet, current.operation);
    }
}

/******************************************************************************
 * Copyright 2015 Qualcomm Technologies International, Ltd.
 ******************************************************************************/
package com.wyzk.lottery.api;


import android.os.Bundle;

import com.csr.csrmesh2.ConfigGatewayApi;
import com.wyzk.lottery.LotteryApplication;
import com.wyzk.lottery.event.MeshRequestEvent;

public class ConfigGateway {

    public static int getProfile() {
        int id = MeshLibraryManager.getInstance().getNextRequestId();
        Bundle data = new Bundle();
        data.putInt(MeshLibraryManager.EXTRA_REQUEST_ID, id);
        LotteryApplication.bus.post(new MeshRequestEvent(MeshRequestEvent.RequestEvent.GATEWAY_GET_PROFILE, data));
        return id;
    }


    public static int removeNetwork() {
        int id = MeshLibraryManager.getInstance().getNextRequestId();
        Bundle data = new Bundle();
        data.putInt(MeshLibraryManager.EXTRA_REQUEST_ID, id);
        LotteryApplication.bus.post(new MeshRequestEvent(MeshRequestEvent.RequestEvent.GATEWAY_REMOVE_NETWORK, data));
        return id;
    }

    static void handleRequest(MeshRequestEvent event) {
        int libId = 0;

        switch (event.what) {
            case GATEWAY_GET_PROFILE:
                libId = ConfigGatewayApi.gatewayProfile();
                break;

            case GATEWAY_REMOVE_NETWORK:
                libId = ConfigGatewayApi.removeNetwork();
                break;
        }

        MeshLibraryManager.getInstance().setRequestIdMapping(libId, event.data.getInt(MeshLibraryManager.EXTRA_REQUEST_ID));
    }
}

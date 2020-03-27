package com.github.leftisttachyon.epsilon;

import com.github.leftisttachyon.epsilon.blob.ContainerModel;

import java.util.HashMap;

/**
 * A service that supplies commands with server and user info.
 *
 * @author Jed Wang
 * @since 1.0.0
 */
public class ServerInfoService {
    /**
     * A {@link HashMap} that stores each {@link ContainerModel} related to each server.
     */
    private HashMap<Integer, ContainerModel> serverContainers;

    /**
     * Creates a new {@link ServerInfoService}.
     */
    public ServerInfoService() {
        serverContainers = new HashMap<>();
    }

    /**
     * Ensures that the server with the given ID is stored in the internal {@link HashMap} for caching.
     *
     * @param serverID the ID of the server
     */
    public void ensureContainer(int serverID) {
        if (!serverContainers.containsKey(serverID)) {
            serverContainers.put(serverID, new ContainerModel(String.valueOf(serverID)));
        }
    }
}

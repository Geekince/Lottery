package com.wyzk.lottery.model;

import java.io.Serializable;
import java.util.List;

public class RoomModel implements Serializable {
    private int total;
    private int records;
    private int page;
    private List<RowModel> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<RowModel> getRows() {
        return rows;
    }

    public void setRows(List<RowModel> rows) {
        this.rows = rows;
    }

    public class RowModel implements Serializable {
        private int roomId;
        private String roomName;
        private String roomAddress;
        private int gameId;
        private String gameName;
        private int roomState;//1:下注中，2封盘，3。结算中 4结算完成  5作废
        private int roundState;//0.准备中 1:下注中，2封盘，3。结算中 4结算完成  5作废
        private String userId;
        private int roomOnline;
        private int percentage;

        public int getRoundState() {
            return roundState;
        }

        public void setRoundState(int roundState) {
            this.roundState = roundState;
        }

        public int getRoomId() {
            return roomId;
        }

        public void setRoomId(int roomId) {
            this.roomId = roomId;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public String getRoomAddress() {
            return roomAddress;
        }

        public void setRoomAddress(String roomAddress) {
            this.roomAddress = roomAddress;
        }

        public int getGameId() {
            return gameId;
        }

        public void setGameId(int gameId) {
            this.gameId = gameId;
        }

        public String getGameName() {
            return gameName;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }

        public int getRoomState() {
            return roomState;
        }

        public void setRoomState(int roomState) {
            this.roomState = roomState;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getRoomOnline() {
            return roomOnline;
        }

        public void setRoomOnline(int roomOnline) {
            this.roomOnline = roomOnline;
        }

        public int getPercentage() {
            return percentage;
        }

        public void setPercentage(int percentage) {
            this.percentage = percentage;
        }

        @Override
        public String toString() {
            return "RowModel{" +
                    "roomId=" + roomId +
                    ", roomName='" + roomName + '\'' +
                    ", roomAddress='" + roomAddress + '\'' +
                    ", gameId=" + gameId +
                    ", gameName='" + gameName + '\'' +
                    ", roomState=" + roomState +
                    ", roundState=" + roundState +
                    ", userId='" + userId + '\'' +
                    ", roomOnline=" + roomOnline +
                    ", percentage=" + percentage +
                    '}';
        }
    }
}

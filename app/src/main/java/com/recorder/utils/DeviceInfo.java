package com.recorder.utils;

import java.io.Serializable;

/**
 * 设备情况
 *
 * @author liaiguo
 */
public class DeviceInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    // 设备ID
    private String ID;
    // 手机型号
    private String Model;
    // 操作系统
    private String OS;
    // App版本
    private String Ver;
    // 1-IOS, 2-ANDROID
    private String Type;
    // MAC地址，无 - None
    private String MAC;
    // 分辨率
    private String RES;
    // 网络运营商
    private String NetOP;
    // 网络类型
    private String NetType;
    // 下载渠道
    private String Download;
    // 用户id
    private String Uid;

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getID() {
        return ID;
    }

    public void setID(String id) {
        ID = id;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getOS() {
        return OS;
    }

    public void setOS(String os) {
        OS = os;
    }

    public String getVer() {
        return Ver;
    }

    public void setVer(String ver) {
        Ver = ver;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String mac) {
        MAC = mac;
    }

    public String getRES() {
        return RES;
    }

    public void setRES(String res) {
        RES = res;
    }

    public String getNetOP() {
        return NetOP;
    }

    public void setNetOP(String netOP) {
        NetOP = netOP;
    }

    public String getNetType() {
        return NetType;
    }

    public void setNetType(String netType) {
        NetType = netType;
    }

    public String getDownload() {
        return Download;
    }

    public void setDownload(String download) {
        Download = download;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((Download == null) ? 0 : Download.hashCode());
        result = prime * result + ((ID == null) ? 0 : ID.hashCode());
        result = prime * result + ((MAC == null) ? 0 : MAC.hashCode());
        result = prime * result + ((Model == null) ? 0 : Model.hashCode());
        result = prime * result + ((NetOP == null) ? 0 : NetOP.hashCode());
        result = prime * result + ((NetType == null) ? 0 : NetType.hashCode());
        result = prime * result + ((OS == null) ? 0 : OS.hashCode());
        result = prime * result + ((RES == null) ? 0 : RES.hashCode());
        result = prime * result + ((Type == null) ? 0 : Type.hashCode());
        result = prime * result + ((Ver == null) ? 0 : Ver.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DeviceInfo other = (DeviceInfo) obj;
        if (Download == null) {
            if (other.Download != null)
                return false;
        } else if (!Download.equals(other.Download))
            return false;
        if (ID == null) {
            if (other.ID != null)
                return false;
        } else if (!ID.equals(other.ID))
            return false;
        if (MAC == null) {
            if (other.MAC != null)
                return false;
        } else if (!MAC.equals(other.MAC))
            return false;
        if (Model == null) {
            if (other.Model != null)
                return false;
        } else if (!Model.equals(other.Model))
            return false;
        if (NetOP == null) {
            if (other.NetOP != null)
                return false;
        } else if (!NetOP.equals(other.NetOP))
            return false;
        if (NetType == null) {
            if (other.NetType != null)
                return false;
        } else if (!NetType.equals(other.NetType))
            return false;
        if (OS == null) {
            if (other.OS != null)
                return false;
        } else if (!OS.equals(other.OS))
            return false;
        if (RES == null) {
            if (other.RES != null)
                return false;
        } else if (!RES.equals(other.RES))
            return false;
        if (Type == null) {
            if (other.Type != null)
                return false;
        } else if (!Type.equals(other.Type))
            return false;
        if (Ver == null) {
            if (other.Ver != null)
                return false;
        } else if (!Ver.equals(other.Ver))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DeviceInfo [ID=" + ID + ", Model=" + Model + ", OS=" + OS
                + ", Ver=" + Ver + ", Type=" + Type + ", MAC=" + MAC + ", RES="
                + RES + ", NetOP=" + NetOP + ", NetType=" + NetType
                + ", Download=" + Download + "]";
    }
}

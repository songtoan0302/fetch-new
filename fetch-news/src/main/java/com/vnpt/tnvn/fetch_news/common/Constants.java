package com.vnpt.tnvn.fetch_news.common;

public interface Constants {

    long CONNECTION_TIMEOUT = 30;

    interface Bean {
        String NEWS_ROOM_JSON = "news_room_json_object";
        String BAO_THANH_NIEN_SETTINGS = "btnSettings";
        String BAO_TIEN_PHONG_SETTINGS = "btpSettings";
        String BAO_THIEU_NIEN_SETTINGS = "thieunienSettings";
        String BAO_TIEN_PHONG_API_SERVICE = "initBtpApiService";
        String BAO_THANH_NIEN_API_SERVICE = "initBtnApiService";
        String BAO_THIEU_NIEN_API_SERVICE = "initThieunienApiService";
    }

    interface Source {
        String BAO_THANH_NIEN = "Thanh niên";
        String BAO_TIEN_PHONG = "Tiền phong";
        String BAO_THIEU_NIEN = "thieunien.vn";
    }

    interface CategoryId {
        long TIEU_DIEM = 21970;
        long CONG_NGHE = 21987;
        long GIOI_TRE = 21985;
    }
}

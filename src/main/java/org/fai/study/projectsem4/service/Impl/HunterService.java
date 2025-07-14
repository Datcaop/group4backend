package org.fai.study.projectsem4.service.Impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HunterService {
    @Value("${hunter.api.key}")
    private String apiKey;
    private final String HUNTER_API_URL = "https://api.hunter.io/v2/email-verifier";

    public boolean validateExistingEmailByHunter(String email) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Tạo URL với tham số
            String url = HUNTER_API_URL + "?email=" + email + "&api_key=" + apiKey;

            // Gửi GET Request và nhận kết quả
            HunterResponse response = restTemplate.getForObject(url, HunterResponse.class);

            // Kiểm tra trạng thái
            if (response != null && response.getData() != null) {
                return response.getData().isValid();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Class để ánh xạ dữ liệu trả về từ API
    public static class HunterResponse {
        private HunterData data;

        public HunterData getData() {
            return data;
        }

        public void setData(HunterData data) {
            this.data = data;
        }
    }

    public static class HunterData {
        private String result;

        public boolean isValid() {
            return "deliverable".equalsIgnoreCase(result);
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
}


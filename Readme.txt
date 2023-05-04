---This project contains processes---

1. Fetch news from newsrooms: ThanhNien, TienPhong, ThieuNien (Hoc tro 360)
2. Refresh tokens for users
3. Save logs of events
4. Send emails to notify questions to people should handle

---Configure (Update properties in file application.properties)---

Each processes has configurations with corresponding prefix, change value of below fields as far as you want:
1. Boolean enable
2. Long scheduleTime
3. String directoryPath
4. String fileConfigPath
5. String baseUrl
6. String refreshApi
7. String clientId
8. String clientSecret
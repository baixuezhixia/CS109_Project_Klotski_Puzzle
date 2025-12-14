package model;

import java.io.*;

public class UserManager {
    private static final int MAX_USERS = 100;
    private static String[] usernames = new String[MAX_USERS];
    private static String[] passwords = new String[MAX_USERS];
    private static int userCount = 0;
    private static final String FILE_NAME = "users.txt";

    // 初始化时从文件加载用户数据
    static {
        loadUsersFromFile();
    }

    public static boolean register(String username, String password) {
        // 检查是否存在
        for (int i = 0; i < userCount; i++) {
            if (usernames[i].equals(username)) {
                return false;
            }
        }
        if (userCount < MAX_USERS) {
            usernames[userCount] = username;
            passwords[userCount] = password;
            userCount++;
            saveUsersToFile(); // 注册成功后保存到文件
            return true;
        }
        return false;
    }

    public static boolean login(String username, String password) {
        for (int i = 0; i < userCount; i++) {
            if (usernames[i].equals(username) && passwords[i].equals(password)) {
                return true;
            }
        }
        return false;
    }

    private static void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < userCount; i++) {
                writer.write(usernames[i] + ":" + passwords[i]);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to save user's data:" + e.getMessage());
        }
    }

    private static void loadUsersFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null && userCount < MAX_USERS) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    usernames[userCount] = parts[0];
                    passwords[userCount] = parts[1];
                    userCount++;
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to load user's data:" + e.getMessage());
        }
    }

    //以上代码不变，新增以下方法
    public static String getSavePath(String username) {
        return username + "_save.dat";
    }
}

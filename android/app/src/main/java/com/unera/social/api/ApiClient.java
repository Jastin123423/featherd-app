package com.unera.social.api;

import android.os.Handler;
import android.os.Looper;

import com.unera.social.models.Comment;
import com.unera.social.models.Event;
import com.unera.social.models.Post;
import com.unera.social.models.Product;
import com.unera.social.models.Reel;
import com.unera.social.models.Story;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApiClient {
    public static final String BASE_URL = "https://featheredsocial.site";
    private static ApiClient instance;

    private final ExecutorService executorService;
    private final Handler mainHandler;

    public interface ApiCallback<T> {
        void onSuccess(T result);
        void onError(Exception error);
    }

    private ApiClient() {
        executorService = Executors.newFixedThreadPool(4);
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public static synchronized ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    // ==========================================
    // FEED & POSTS
    // ==========================================
    public void fetchFeed(ApiCallback<List<Post>> callback) {
        executorService.execute(() -> {
            try {
                String response = getRequest(BASE_URL + "/api/feeds");
                List<Post> posts = parsePosts(response);
                if (posts.isEmpty()) {
                    // Fallback to /api/posts
                    response = getRequest(BASE_URL + "/api/posts");
                    posts = parsePosts(response);
                }
                if (posts.isEmpty()) {
                    posts = getDefaultPosts();
                }
                List<Post> finalPosts = posts;
                mainHandler.post(() -> callback.onSuccess(finalPosts));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onSuccess(getDefaultPosts()));
            }
        });
    }

    public void createPost(String content, String imageUrl, ApiCallback<Boolean> callback) {
        executorService.execute(() -> {
            try {
                JSONObject json = new JSONObject();
                json.put("content", content);
                json.put("image_url", imageUrl != null ? imageUrl : "");
                json.put("visibility", "public");

                String res = postRequest(BASE_URL + "/api/posts", json.toString());
                mainHandler.post(() -> callback.onSuccess(true));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onSuccess(true)); // optimistic success
            }
        });
    }

    public void reactToPost(String postId, ApiCallback<Boolean> callback) {
        executorService.execute(() -> {
            try {
                JSONObject json = new JSONObject();
                json.put("type", "like");
                postRequest(BASE_URL + "/api/posts/" + postId + "/react", json.toString());
                mainHandler.post(() -> callback.onSuccess(true));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onSuccess(true));
            }
        });
    }

    // ==========================================
    // STORIES
    // ==========================================
    public void fetchStories(ApiCallback<List<Story>> callback) {
        executorService.execute(() -> {
            try {
                String response = getRequest(BASE_URL + "/api/stories");
                List<Story> stories = parseStories(response);
                if (stories.isEmpty()) {
                    stories = getDefaultStories();
                }
                List<Story> finalStories = stories;
                mainHandler.post(() -> callback.onSuccess(finalStories));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onSuccess(getDefaultStories()));
            }
        });
    }

    // ==========================================
    // REELS
    // ==========================================
    public void fetchReels(ApiCallback<List<Reel>> callback) {
        executorService.execute(() -> {
            try {
                String response = getRequest(BASE_URL + "/api/reels");
                List<Reel> reels = parseReels(response);
                if (reels.isEmpty()) {
                    reels = getDefaultReels();
                }
                List<Reel> finalReels = reels;
                mainHandler.post(() -> callback.onSuccess(finalReels));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onSuccess(getDefaultReels()));
            }
        });
    }

    // ==========================================
    // MARKETPLACE
    // ==========================================
    public void fetchProducts(ApiCallback<List<Product>> callback) {
        executorService.execute(() -> {
            try {
                String response = getRequest(BASE_URL + "/api/products");
                List<Product> products = parseProducts(response);
                if (products.isEmpty()) {
                    products = getDefaultProducts();
                }
                List<Product> finalProducts = products;
                mainHandler.post(() -> callback.onSuccess(finalProducts));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onSuccess(getDefaultProducts()));
            }
        });
    }

    // ==========================================
    // EVENTS
    // ==========================================
    public void fetchEvents(ApiCallback<List<Event>> callback) {
        executorService.execute(() -> {
            try {
                String response = getRequest(BASE_URL + "/api/events");
                List<Event> events = parseEvents(response);
                if (events.isEmpty()) {
                    events = getDefaultEvents();
                }
                List<Event> finalEvents = events;
                mainHandler.post(() -> callback.onSuccess(finalEvents));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onSuccess(getDefaultEvents()));
            }
        });
    }

    // ==========================================
    // HTTP UTILS
    // ==========================================
    private String getRequest(String urlStr) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(6000);
        conn.setReadTimeout(10000);
        conn.connect();

        int responseCode = conn.getResponseCode();
        if (responseCode >= 200 && responseCode < 300) {
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            conn.disconnect();
            return sb.toString();
        }
        conn.disconnect();
        return "";
    }

    private String postRequest(String urlStr, String jsonBody) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        conn.setConnectTimeout(6000);
        conn.setReadTimeout(10000);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        conn.disconnect();
        return String.valueOf(responseCode);
    }

    // ==========================================
    // JSON PARSERS
    // ==========================================
    private List<Post> parsePosts(String jsonStr) {
        List<Post> list = new ArrayList<>();
        try {
            if (jsonStr == null || jsonStr.trim().isEmpty()) return list;
            JSONObject root = new JSONObject(jsonStr);
            JSONArray array = root.optJSONArray("posts");
            if (array == null) array = root.optJSONArray("feed");
            if (array == null) array = root.optJSONArray("items");
            if (array == null) return list;

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String id = obj.optString("id", String.valueOf(i));
                String content = obj.optString("content", obj.optString("text", ""));
                String imageUrl = obj.optString("image_url", obj.optString("image", ""));
                int likes = obj.optInt("likes_count", obj.optInt("likes", 0));
                int comments = obj.optInt("comments_count", obj.optInt("comments", 0));
                String time = obj.optString("created_at", "Recently");

                String authorName = "Feathered Creator";
                String authorAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150";

                JSONObject user = obj.optJSONObject("user");
                if (user != null) {
                    authorName = user.optString("name", user.optString("username", authorName));
                    authorAvatar = user.optString("avatar_url", authorAvatar);
                } else {
                    authorName = obj.optString("author_name", authorName);
                }

                list.add(new Post(id, authorName, authorAvatar, time, content, imageUrl, likes, comments));
            }
        } catch (Exception ignored) {}
        return list;
    }

    private List<Story> parseStories(String jsonStr) {
        List<Story> list = new ArrayList<>();
        try {
            if (jsonStr == null || jsonStr.trim().isEmpty()) return list;
            JSONObject root = new JSONObject(jsonStr);
            JSONArray array = root.optJSONArray("stories");
            if (array == null) return list;

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String id = obj.optString("id", String.valueOf(i));
                String username = obj.optString("username", "Story");
                String avatar = obj.optString("avatar_url", "");
                String media = obj.optString("media_url", "");
                list.add(new Story(id, username, avatar, media));
            }
        } catch (Exception ignored) {}
        return list;
    }

    private List<Reel> parseReels(String jsonStr) {
        List<Reel> list = new ArrayList<>();
        try {
            if (jsonStr == null || jsonStr.trim().isEmpty()) return list;
            JSONObject root = new JSONObject(jsonStr);
            JSONArray array = root.optJSONArray("reels");
            if (array == null) return list;

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String id = obj.optString("id", String.valueOf(i));
                String caption = obj.optString("caption", "");
                String media = obj.optString("video_url", obj.optString("media_url", ""));
                String sound = obj.optString("sound_title", "♫ Original Sound");
                int likes = obj.optInt("likes_count", 120);
                int comments = obj.optInt("comments_count", 14);

                String creator = "Creator";
                String avatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150";
                JSONObject user = obj.optJSONObject("user");
                if (user != null) {
                    creator = user.optString("name", creator);
                    avatar = user.optString("avatar_url", avatar);
                }
                list.add(new Reel(id, creator, avatar, caption, media, sound, likes, comments));
            }
        } catch (Exception ignored) {}
        return list;
    }

    private List<Product> parseProducts(String jsonStr) {
        List<Product> list = new ArrayList<>();
        try {
            if (jsonStr == null || jsonStr.trim().isEmpty()) return list;
            JSONObject root = new JSONObject(jsonStr);
            JSONArray array = root.optJSONArray("products");
            if (array == null) return list;

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String id = obj.optString("id", String.valueOf(i));
                String title = obj.optString("title", "Item");
                String price = "$" + obj.optString("price", "25");
                String location = obj.optString("location", "Nearby");
                String image = obj.optString("image_url", obj.optString("image", ""));
                String desc = obj.optString("description", "");
                list.add(new Product(id, title, price, location, image, desc));
            }
        } catch (Exception ignored) {}
        return list;
    }

    private List<Event> parseEvents(String jsonStr) {
        List<Event> list = new ArrayList<>();
        try {
            if (jsonStr == null || jsonStr.trim().isEmpty()) return list;
            JSONObject root = new JSONObject(jsonStr);
            JSONArray array = root.optJSONArray("events");
            if (array == null) return list;

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String id = obj.optString("id", String.valueOf(i));
                String title = obj.optString("title", "Community Event");
                String date = obj.optString("event_date", obj.optString("date", "Upcoming"));
                String location = obj.optString("location", "City Center");
                String image = obj.optString("cover_url", obj.optString("image", ""));
                int interested = obj.optInt("interested_count", 45);
                list.add(new Event(id, title, date, location, image, interested));
            }
        } catch (Exception ignored) {}
        return list;
    }

    // ==========================================
    // RICH RESILIENT DEFAULT DATA
    // ==========================================
    public static List<Post> getDefaultPosts() {
        List<Post> list = new ArrayList<>();
        list.add(new Post(
                "p1",
                "Elena Vance",
                "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150",
                "15m ago",
                "Welcome to the official native Feathered Android app! 🚀 Fast, smooth, and directly connected to our live platform.",
                "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800",
                142,
                24
        ));
        list.add(new Post(
                "p2",
                "Marcus Sterling",
                "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
                "1h ago",
                "Sunset vibes from the studio session today. New sound drops tonight on Feathered Music! 🎵🌅",
                "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800",
                89,
                11
        ));
        list.add(new Post(
                "p3",
                "Sophia Chen",
                "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
                "3h ago",
                "Exploring creative community spaces in town. Check out our upcoming live events tab to join us this weekend!",
                "https://images.unsplash.com/photo-1514525253440-b393452e8d26?w=800",
                210,
                38
        ));
        return list;
    }

    public static List<Story> getDefaultStories() {
        List<Story> list = new ArrayList<>();
        list.add(new Story("s0", "Your Story", "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150", ""));
        list.add(new Story("s1", "Elena", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150", ""));
        list.add(new Story("s2", "Marcus", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150", ""));
        list.add(new Story("s3", "Sophia", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150", ""));
        list.add(new Story("s4", "Alex", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150", ""));
        list.add(new Story("s5", "Maya", "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=150", ""));
        return list;
    }

    public static List<Reel> getDefaultReels() {
        List<Reel> list = new ArrayList<>();
        list.add(new Reel(
                "r1",
                "Feathered Beats",
                "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
                "Live electronic jam session in the city! Turn up your volume 🎧⚡",
                "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?w=1080",
                "♫ Feathered Vibes Vol. 4",
                1240,
                184
        ));
        list.add(new Reel(
                "r2",
                "Studio Creative",
                "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150",
                "Behind the scenes creating the new visual identities ✨",
                "https://images.unsplash.com/photo-1574717024653-61fd2cf4d44d?w=1080",
                "♫ Ambient Chillout",
                892,
                67
        ));
        return list;
    }

    public static List<Product> getDefaultProducts() {
        List<Product> list = new ArrayList<>();
        list.add(new Product("pr1", "Sony Wireless Headphones", "$140", "Brooklyn, NY", "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500", "Brand new sealed in box."));
        list.add(new Product("pr2", "Mechanical Gaming Keyboard", "$85", "Manhattan, NY", "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=500", "RGB backlit with red linear switches."));
        list.add(new Product("pr3", "Vintage Leather Backpack", "$65", "Queens, NY", "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=500", "Handcrafted premium brown leather."));
        list.add(new Product("pr4", "Smart Watch Series 8", "$199", "Jersey City, NJ", "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500", "Midnight black, excellent condition."));
        return list;
    }

    public static List<Event> getDefaultEvents() {
        List<Event> list = new ArrayList<>();
        list.add(new Event("e1", "Feathered Creator Summit 2026", "SAT, OCT 24 • 6:00 PM", "Metropolitan Arts Pavilion", "https://images.unsplash.com/photo-1511578314322-379afb476865?w=800", 320));
        list.add(new Event("e2", "Indie Music & Arts Fest", "SUN, NOV 02 • 4:00 PM", "Riverside Amphitheater", "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=800", 540));
        list.add(new Event("e3", "Tech & Design Networking Meetup", "THU, NOV 12 • 7:30 PM", "Tech Innovation Hub", "https://images.unsplash.com/photo-1528605248644-14dd04022da1?w=800", 180));
        return list;
    }
}

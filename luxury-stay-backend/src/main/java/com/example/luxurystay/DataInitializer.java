package com.example.luxurystay;

import com.example.luxurystay.entity.*;
import com.example.luxurystay.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private OrderDisputeRepository orderDisputeRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        logger.info("=== DataInitializer started ===");
        logger.info("Users count: {}, Cities count: {}, Hotels count: {}", 
            userRepository.count(), cityRepository.count(), hotelRepository.count());
        
        if (userRepository.count() == 0) {
            logger.info("Initializing users...");
            initializeUsers();
            logger.info("Users initialized. Count: {}", userRepository.count());
        }
        if (cityRepository.count() == 0) {
            logger.info("Initializing cities...");
            initializeCities();
            logger.info("Cities initialized. Count: {}", cityRepository.count());
        }
        if (hotelRepository.count() == 0) {
            logger.info("Initializing hotels...");
            initializeHotels();
            logger.info("Hotels initialized. Count: {}", hotelRepository.count());
        }
        if (bookingRepository.count() == 0) {
            logger.info("Initializing bookings...");
            initializeBookings();
            logger.info("Bookings initialized. Count: {}", bookingRepository.count());
        }
        if (reviewRepository.count() == 0) {
            logger.info("Initializing reviews...");
            initializeReviews();
            logger.info("Reviews initialized. Count: {}", reviewRepository.count());
        }
        if (wishlistRepository.count() == 0) {
            logger.info("Initializing wishlists...");
            initializeWishlists();
            logger.info("Wishlists initialized. Count: {}", wishlistRepository.count());
        }
        if (orderDisputeRepository.count() == 0) {
            logger.info("Initializing disputes...");
            initializeDisputes();
            logger.info("Disputes initialized. Count: {}", orderDisputeRepository.count());
        }
        logger.info("=== DataInitializer completed ===");
    }

    private void initializeUsers() {
        User admin = User.builder()
                .name("管理员")
                .email("admin@example.com")
                .password(passwordEncoder.encode("123456"))
                .userType(UserType.ADMIN)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(admin);

        User business = User.builder()
                .name("商家管理员")
                .email("business@163.com")
                .password(passwordEncoder.encode("123456"))
                .userType(UserType.BUSINESS)
                .companyName("奢华度假酒店集团")
                .businessLicenseVerified(true)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(business);

        User business2 = User.builder()
                .name("东方明珠酒店集团")
                .email("business2@163.com")
                .password(passwordEncoder.encode("123456"))
                .userType(UserType.BUSINESS)
                .companyName("东方明珠酒店集团")
                .businessLicenseVerified(true)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(business2);

        User business3 = User.builder()
                .name("盛世华亭酒店集团")
                .email("business3@163.com")
                .password(passwordEncoder.encode("123456"))
                .userType(UserType.BUSINESS)
                .companyName("盛世华亭酒店集团")
                .businessLicenseVerified(true)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(business3);

        User business4 = User.builder()
                .name("云端假日酒店集团")
                .email("business4@163.com")
                .password(passwordEncoder.encode("123456"))
                .userType(UserType.BUSINESS)
                .companyName("云端假日酒店集团")
                .businessLicenseVerified(true)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(business4);

        User user1 = User.builder()
                .name("张三")
                .email("test@163.com")
                .password(passwordEncoder.encode("123456"))
                .userType(UserType.USER)
                .phone("13800138001")
                .gender("MALE")
                .birthDate(LocalDate.of(1990, 1, 15))
                .address("北京市朝阳区")
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .name("李四")
                .email("lisi@example.com")
                .password(passwordEncoder.encode("123456"))
                .userType(UserType.USER)
                .phone("13800138002")
                .gender("FEMALE")
                .birthDate(LocalDate.of(1992, 5, 20))
                .address("上海市浦东新区")
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user2);

        User user3 = User.builder()
                .name("王五")
                .email("wangwu@example.com")
                .password(passwordEncoder.encode("123456"))
                .userType(UserType.USER)
                .phone("13800138003")
                .gender("MALE")
                .birthDate(LocalDate.of(1988, 10, 8))
                .address("广州市天河区")
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user3);

        System.out.println("Sample users created successfully!");
    }

    private void initializeHotels() {
        List<User> businesses = userRepository.findByUserType(UserType.BUSINESS);
        List<City> cities = cityRepository.findAll();
        
        if (businesses.isEmpty()) {
            System.out.println("No businesses found, skipping hotel initialization");
            return;
        }
        
        if (cities.isEmpty()) {
            System.out.println("No cities found, skipping hotel initialization");
            return;
        }

        String[] hotelNames = {"豪华大酒店", "皇冠假日酒店", "国际酒店", "度假酒店", "精品酒店"};
        String[] descriptions = {
            "位于市中心繁华地段，交通便利，设施齐全，是商务出行的理想选择。",
            "拥有精致的客房和完善的会议设施，提供贴心周到的服务。",
            "融合现代设计与本地文化特色，为您打造独特的入住体验。",
            "周边景点众多，是休闲度假的完美目的地。",
            "提供私人管家服务，享受尊贵奢华的入住体验。"
        };
        String[] imageUrls = {
            "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800&h=600&fit=crop",
            "https://images.unsplash.com/photo-1582719508461-905c673771fd?w=800&h=600&fit=crop",
            "https://images.unsplash.com/photo-1571896349842-33c89424de2d?w=800&h=600&fit=crop",
            "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=800&h=600&fit=crop",
            "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=800&h=600&fit=crop"
        };
        
        int hotelCount = 0;
        for (City city : cities) {
            for (int i = 0; i < 5; i++) {
                User business = businesses.get(i % businesses.size());
                int star = (int) (Math.random() * 2) + 4;
                double rating = Math.round((4.0 + Math.random() * 1.0) * 10) / 10.0;
                int price = (int) (300 + Math.random() * 2000);
                int roomsAvailable = (int) (30 + Math.random() * 200);
                int reviewCount = (int) (Math.random() * 500);
                
                Hotel hotel = Hotel.builder()
                        .name(city.getName() + hotelNames[i])
                        .location(city.getProvince() + city.getName() + "市" + (i + 1) + "号路" + (100 + i) + "号")
                        .city(city.getName())
                        .description(city.getName() + descriptions[i])
                        .image(imageUrls[i])
                        .star(star)
                        .rating(rating)
                        .price(price)
                        .facilities("免费WiFi,游泳池,健身房,SPA,商务中心,餐厅,酒吧,停车场")
                        .reviewCount(reviewCount)
                        .roomsAvailable(roomsAvailable)
                        .businessId(business.getId())
                        .depositAmount((int)(price * 0.2))
                        .depositPolicy("预订需支付首晚房费作为定金，入住前7天取消可全额退款")
                        .noShowPolicy("未入住且未提前取消，定金不予退款")
                        .commissionRate(10)
                        .status("ACTIVE")
                        .createdAt(LocalDateTime.now())
                        .build();
                hotelRepository.save(hotel);
                hotelCount++;
            }
        }

        System.out.println("Sample hotels created successfully! Total: " + hotelCount);
    }


    private void initializeBookings() {
        List<User> users = userRepository.findByUserType(UserType.USER);
        List<Hotel> hotels = hotelRepository.findAll();

        if (users.size() >= 3 && hotels.size() >= 5) {
            User user1 = users.get(0);
            User user2 = users.get(1);
            User user3 = users.get(2);

            Booking booking1 = Booking.builder()
                    .userId(user1.getId())
                    .hotelId(hotels.get(0).getId())
                    .checkIn(LocalDate.now().plusDays(7))
                    .checkOut(LocalDate.now().plusDays(10))
                    .adults(2)
                    .children(0)
                    .roomType("标准间")
                    .guests(2)
                    .guestName(user1.getName())
                    .phone(user1.getPhone())
                    .totalPrice(hotels.get(0).getPrice() * 3)
                    .status(BookingStatus.CONFIRMED)
                    .bookingNumber("BK" + System.currentTimeMillis())
                    .createdAt(LocalDateTime.now())
                    .build();
            bookingRepository.save(booking1);

            Booking booking2 = Booking.builder()
                    .userId(user1.getId())
                    .hotelId(hotels.get(1).getId())
                    .checkIn(LocalDate.now().plusDays(14))
                    .checkOut(LocalDate.now().plusDays(16))
                    .adults(2)
                    .children(1)
                    .roomType("家庭房")
                    .guests(3)
                    .guestName(user1.getName())
                    .phone(user1.getPhone())
                    .totalPrice(hotels.get(1).getPrice() * 2)
                    .status(BookingStatus.PENDING)
                    .bookingNumber("BK" + (System.currentTimeMillis() + 1))
                    .createdAt(LocalDateTime.now())
                    .build();
            bookingRepository.save(booking2);

            Booking booking3 = Booking.builder()
                    .userId(user1.getId())
                    .hotelId(hotels.get(2).getId())
                    .checkIn(LocalDate.now().minusDays(10))
                    .checkOut(LocalDate.now().minusDays(7))
                    .adults(2)
                    .children(0)
                    .roomType("豪华间")
                    .guests(2)
                    .guestName(user1.getName())
                    .phone(user1.getPhone())
                    .totalPrice(hotels.get(2).getPrice() * 3)
                    .status(BookingStatus.COMPLETED)
                    .bookingNumber("BK" + (System.currentTimeMillis() + 2))
                    .createdAt(LocalDateTime.now().minusDays(15))
                    .build();
            bookingRepository.save(booking3);

            Booking booking4 = Booking.builder()
                    .userId(user2.getId())
                    .hotelId(hotels.get(3).getId())
                    .checkIn(LocalDate.now().plusDays(5))
                    .checkOut(LocalDate.now().plusDays(8))
                    .adults(1)
                    .children(0)
                    .roomType("大床房")
                    .guests(1)
                    .guestName(user2.getName())
                    .phone(user2.getPhone())
                    .totalPrice(hotels.get(3).getPrice() * 3)
                    .status(BookingStatus.PENDING)
                    .bookingNumber("BK" + (System.currentTimeMillis() + 3))
                    .createdAt(LocalDateTime.now().minusHours(2))
                    .build();
            bookingRepository.save(booking4);

            Booking booking5 = Booking.builder()
                    .userId(user2.getId())
                    .hotelId(hotels.get(4).getId())
                    .checkIn(LocalDate.now().plusDays(10))
                    .checkOut(LocalDate.now().plusDays(12))
                    .adults(2)
                    .children(0)
                    .roomType("标准间")
                    .guests(2)
                    .guestName(user2.getName())
                    .phone(user2.getPhone())
                    .totalPrice(hotels.get(4).getPrice() * 2)
                    .status(BookingStatus.CONFIRMED)
                    .bookingNumber("BK" + (System.currentTimeMillis() + 4))
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .build();
            bookingRepository.save(booking5);

            Booking booking6 = Booking.builder()
                    .userId(user3.getId())
                    .hotelId(hotels.get(0).getId())
                    .checkIn(LocalDate.now().minusDays(20))
                    .checkOut(LocalDate.now().minusDays(18))
                    .adults(2)
                    .children(1)
                    .roomType("家庭房")
                    .guests(3)
                    .guestName(user3.getName())
                    .phone(user3.getPhone())
                    .totalPrice(hotels.get(0).getPrice() * 2)
                    .status(BookingStatus.COMPLETED)
                    .bookingNumber("BK" + (System.currentTimeMillis() + 5))
                    .createdAt(LocalDateTime.now().minusDays(25))
                    .build();
            bookingRepository.save(booking6);

            Booking booking7 = Booking.builder()
                    .userId(user3.getId())
                    .hotelId(hotels.get(5).getId())
                    .checkIn(LocalDate.now().plusDays(3))
                    .checkOut(LocalDate.now().plusDays(6))
                    .adults(2)
                    .children(0)
                    .roomType("豪华间")
                    .guests(2)
                    .guestName(user3.getName())
                    .phone(user3.getPhone())
                    .totalPrice(hotels.get(5).getPrice() * 3)
                    .status(BookingStatus.PENDING)
                    .bookingNumber("BK" + (System.currentTimeMillis() + 6))
                    .createdAt(LocalDateTime.now().minusHours(5))
                    .build();
            bookingRepository.save(booking7);

            Booking booking8 = Booking.builder()
                    .userId(user1.getId())
                    .hotelId(hotels.get(6).getId())
                    .checkIn(LocalDate.now().minusDays(5))
                    .checkOut(LocalDate.now().minusDays(3))
                    .adults(1)
                    .children(0)
                    .roomType("大床房")
                    .guests(1)
                    .guestName(user1.getName())
                    .phone(user1.getPhone())
                    .totalPrice(hotels.get(6).getPrice() * 2)
                    .status(BookingStatus.USER_CANCELLED)
                    .bookingNumber("BK" + (System.currentTimeMillis() + 7))
                    .createdAt(LocalDateTime.now().minusDays(10))
                    .build();
            bookingRepository.save(booking8);

            Booking booking9 = Booking.builder()
                    .userId(user2.getId())
                    .hotelId(hotels.get(7).getId())
                    .checkIn(LocalDate.now().plusDays(20))
                    .checkOut(LocalDate.now().plusDays(22))
                    .adults(2)
                    .children(0)
                    .roomType("标准间")
                    .guests(2)
                    .guestName(user2.getName())
                    .phone(user2.getPhone())
                    .totalPrice(hotels.get(7).getPrice() * 2)
                    .status(BookingStatus.CONFIRMED)
                    .bookingNumber("BK" + (System.currentTimeMillis() + 8))
                    .createdAt(LocalDateTime.now().minusDays(3))
                    .build();
            bookingRepository.save(booking9);

            Booking booking10 = Booking.builder()
                    .userId(user3.getId())
                    .hotelId(hotels.get(8).getId())
                    .checkIn(LocalDate.now().minusDays(8))
                    .checkOut(LocalDate.now().minusDays(5))
                    .adults(2)
                    .children(0)
                    .roomType("豪华间")
                    .guests(2)
                    .guestName(user3.getName())
                    .phone(user3.getPhone())
                    .totalPrice(hotels.get(8).getPrice() * 3)
                    .status(BookingStatus.COMPLETED)
                    .bookingNumber("BK" + (System.currentTimeMillis() + 9))
                    .createdAt(LocalDateTime.now().minusDays(13))
                    .build();
            bookingRepository.save(booking10);
        }

        System.out.println("Sample bookings created successfully!");
    }

    private void initializeReviews() {
        List<User> users = userRepository.findByUserType(UserType.USER);
        List<Hotel> hotels = hotelRepository.findAll();

        if (users.size() >= 3 && hotels.size() >= 6) {
            User user1 = users.get(0);
            User user2 = users.get(1);
            User user3 = users.get(2);

            Review review1 = Review.builder()
                    .userId(user1.getId())
                    .userName(user1.getName())
                    .hotelId(hotels.get(0).getId())
                    .rating(5)
                    .title("非常棒的酒店体验")
                    .content("非常棒的酒店！服务一流，房间宽敞干净，早餐丰富。位置也很方便，下次还会再来！")
                    .status(ReviewStatus.APPROVED)
                    .createdAt(LocalDateTime.now().minusDays(5))
                    .build();
            reviewRepository.save(review1);

            Review review2 = Review.builder()
                    .userId(user1.getId())
                    .userName(user1.getName())
                    .hotelId(hotels.get(1).getId())
                    .rating(4)
                    .title("环境很好的酒店")
                    .content("酒店环境很好，设施齐全，但服务响应速度有待提高。总体来说是一次愉快的入住体验。")
                    .status(ReviewStatus.APPROVED)
                    .createdAt(LocalDateTime.now().minusDays(3))
                    .build();
            reviewRepository.save(review2);

            Review review3 = Review.builder()
                    .userId(user1.getId())
                    .userName(user1.getName())
                    .hotelId(hotels.get(2).getId())
                    .rating(5)
                    .title("度假首选")
                    .content("度假首选！私人海滩很美，水上乐园孩子们玩得很开心。SPA也很专业，强烈推荐！")
                    .status(ReviewStatus.APPROVED)
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .build();
            reviewRepository.save(review3);

            Review review4 = Review.builder()
                    .userId(user2.getId())
                    .userName(user2.getName())
                    .hotelId(hotels.get(3).getId())
                    .rating(4)
                    .title("商务出行首选")
                    .content("商务出行首选！地理位置优越，交通便利。会议室设施先进，服务周到。")
                    .status(ReviewStatus.APPROVED)
                    .createdAt(LocalDateTime.now().minusDays(2))
                    .build();
            reviewRepository.save(review4);

            Review review5 = Review.builder()
                    .userId(user2.getId())
                    .userName(user2.getName())
                    .hotelId(hotels.get(4).getId())
                    .rating(5)
                    .title("完美的周末度假")
                    .content("完美的周末度假！酒店服务无可挑剔，房间视野很好，能看到城市全景。")
                    .status(ReviewStatus.APPROVED)
                    .createdAt(LocalDateTime.now().minusDays(4))
                    .build();
            reviewRepository.save(review5);

            Review review6 = Review.builder()
                    .userId(user1.getId())
                    .userName(user1.getName())
                    .hotelId(hotels.get(5).getId())
                    .rating(3)
                    .title("体验一般")
                    .content("整体体验一般，房间隔音效果不太好，早餐种类较少。希望能够改进。")
                    .status(ReviewStatus.APPROVED)
                    .createdAt(LocalDateTime.now().minusDays(6))
                    .build();
            reviewRepository.save(review6);

            Review review7 = Review.builder()
                    .userId(user3.getId())
                    .userName(user3.getName())
                    .hotelId(hotels.get(6).getId())
                    .rating(5)
                    .title("超五星体验")
                    .content("服务超五星！从入住到退房，每个环节都非常专业。房间设施一流，早餐丰富多样。")
                    .status(ReviewStatus.APPROVED)
                    .createdAt(LocalDateTime.now().minusDays(7))
                    .build();
            reviewRepository.save(review7);

            Review review8 = Review.builder()
                    .userId(user2.getId())
                    .userName(user2.getName())
                    .hotelId(hotels.get(7).getId())
                    .rating(4)
                    .title("性价比很高")
                    .content("性价比很高的酒店，位置好，服务好，价格合理。下次还会选择这里。")
                    .status(ReviewStatus.APPROVED)
                    .createdAt(LocalDateTime.now().minusDays(8))
                    .build();
            reviewRepository.save(review8);

            Review review9 = Review.builder()
                    .userId(user3.getId())
                    .userName(user3.getName())
                    .hotelId(hotels.get(8).getId())
                    .rating(5)
                    .title("完美的蜜月之旅")
                    .content("完美的蜜月之旅！酒店为我们准备了特别的惊喜，服务贴心周到，房间浪漫温馨。")
                    .status(ReviewStatus.APPROVED)
                    .createdAt(LocalDateTime.now().minusDays(9))
                    .build();
            reviewRepository.save(review9);

            Review review10 = Review.builder()
                    .userId(user1.getId())
                    .userName(user1.getName())
                    .hotelId(hotels.get(9).getId())
                    .rating(4)
                    .title("不错的商务酒店")
                    .content("不错的商务酒店，设施齐全，服务专业。早餐种类丰富，位置便利。")
                    .status(ReviewStatus.APPROVED)
                    .createdAt(LocalDateTime.now().minusDays(10))
                    .build();
            reviewRepository.save(review10);

            Review review11 = Review.builder()
                    .userId(user3.getId())
                    .userName(user3.getName())
                    .hotelId(hotels.get(0).getId())
                    .rating(1)
                    .title("非常失望")
                    .content("非常失望！房间脏乱差，服务态度恶劣，完全不符合五星级酒店的标准。")
                    .status(ReviewStatus.REJECTED)
                    .createdAt(LocalDateTime.now().minusDays(2))
                    .build();
            reviewRepository.save(review11);

            Review review12 = Review.builder()
                    .userId(user2.getId())
                    .userName(user2.getName())
                    .hotelId(hotels.get(1).getId())
                    .rating(2)
                    .title("不太满意")
                    .content("房间太小，隔音效果差，晚上很吵。价格偏高，性价比一般。")
                    .status(ReviewStatus.REJECTED)
                    .createdAt(LocalDateTime.now().minusDays(4))
                    .build();
            reviewRepository.save(review12);
        }

        System.out.println("Sample reviews created successfully!");
    }

    private void initializeWishlists() {
        List<User> users = userRepository.findByUserType(UserType.USER);
        List<Hotel> hotels = hotelRepository.findAll();

        if (!users.isEmpty() && hotels.size() >= 5) {
            User user = users.get(0);
            for (int i = 0; i < 5; i++) {
                Wishlist wishlist = Wishlist.builder()
                        .userId(user.getId())
                        .hotelId(hotels.get(i).getId())
                        .createdAt(LocalDateTime.now())
                        .build();
                wishlistRepository.save(wishlist);
            }
        }

        System.out.println("Sample wishlists created successfully!");
    }

    private void initializeDisputes() {
        List<Booking> bookings = bookingRepository.findAll();
        List<User> users = userRepository.findByUserType(UserType.USER);
        List<User> businesses = userRepository.findByUserType(UserType.BUSINESS);
        
        if (bookings.size() >= 3 && users.size() >= 3 && !businesses.isEmpty()) {
            OrderDispute dispute1 = OrderDispute.builder()
                    .bookingId(bookings.get(0).getId())
                    .applicantId(users.get(0).getId())
                    .applicantType(UserType.USER)
                    .reason("酒店设施与描述不符，房间空调无法正常使用，要求全额退款")
                    .evidence("提供了房间照片和与客服沟通记录")
                    .evidenceImages("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=broken%20air%20conditioner%20in%20hotel%20room,https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=hotel%20room%20temperature%20display%20high")
                    .communicationRecords("USER---2026-06-20 10:30---房间空调坏了，根本没法住|||SUPPORT---2026-06-20 10:35---抱歉给您带来不便，我马上联系酒店核实|||USER---2026-06-20 10:40---已经等了半小时了，空调还是不行|||SUPPORT---2026-06-20 10:45---酒店表示可以换房，但您坚持要退款")
                    .status(DisputeStatus.PENDING)
                    .createdAt(LocalDateTime.now().minusDays(2))
                    .build();
            orderDisputeRepository.save(dispute1);

            OrderDispute dispute2 = OrderDispute.builder()
                    .bookingId(bookings.get(1).getId())
                    .applicantId(users.get(1).getId())
                    .applicantType(UserType.USER)
                    .reason("预订后价格下降，要求退还差价")
                    .evidence("提供了价格截图和预订确认邮件")
                    .evidenceImages("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=hotel%20booking%20price%20comparison%20screenshot,https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=email%20confirmation%20for%20hotel%20booking")
                    .communicationRecords("USER---2026-06-21 14:00---我昨天预订的房间今天降价了200元|||SUPPORT---2026-06-21 14:05---让我帮您查一下价格变动情况|||USER---2026-06-21 14:10---截图已经发给你们了，希望能退还差价")
                    .status(DisputeStatus.PENDING)
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .build();
            orderDisputeRepository.save(dispute2);

            OrderDispute dispute3 = OrderDispute.builder()
                    .bookingId(bookings.get(2).getId())
                    .applicantId(users.get(2).getId())
                    .applicantType(UserType.USER)
                    .reason("酒店卫生条件差，床单有污渍")
                    .evidence("提供了现场照片")
                    .evidenceImages("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=dirty%20stained%20bed%20sheet%20in%20hotel,https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=unsanitary%20hotel%20bathroom")
                    .communicationRecords("USER---2026-06-17 22:00---房间床单有明显污渍，太恶心了|||SUPPORT---2026-06-17 22:10---非常抱歉，我们立即处理|||USER---2026-06-17 22:15---已经换了床单，但还是有异味|||SUPPORT---2026-06-17 22:20---已记录问题，正在联系酒店管理层")
                    .status(DisputeStatus.PROCESSING)
                    .adminNote("已联系酒店核实情况，等待回复")
                    .adminId(1L)
                    .createdAt(LocalDateTime.now().minusDays(5))
                    .build();
            orderDisputeRepository.save(dispute3);

            OrderDispute dispute4 = OrderDispute.builder()
                    .bookingId(bookings.size() > 3 ? bookings.get(3).getId() : bookings.get(0).getId())
                    .applicantId(users.get(0).getId())
                    .applicantType(UserType.USER)
                    .reason("酒店拒绝入住，要求全额退款")
                    .evidence("提供了入住记录和沟通截图")
                    .evidenceImages("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=hotel%20reception%20desk%20refusing%20check-in,https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=phone%20conversation%20log%20with%20hotel")
                    .communicationRecords("USER---2026-06-15 18:00---我到酒店了，但前台说没有我的预订|||SUPPORT---2026-06-15 18:05---请稍等，我帮您查询|||USER---2026-06-15 18:15---酒店说系统里查不到我的订单")
                    .status(DisputeStatus.RESOLVED)
                    .adminNote("经核实，酒店已承认问题并同意全额退款")
                    .adminId(1L)
                    .resolvedAt(LocalDateTime.now().minusDays(3))
                    .createdAt(LocalDateTime.now().minusDays(7))
                    .build();
            orderDisputeRepository.save(dispute4);

            OrderDispute dispute5 = OrderDispute.builder()
                    .bookingId(bookings.size() > 4 ? bookings.get(4).getId() : bookings.get(1).getId())
                    .applicantId(users.get(1).getId())
                    .applicantType(UserType.USER)
                    .reason("要求无理由全额退款")
                    .evidence("无有效证据")
                    .evidenceImages("")
                    .communicationRecords("USER---2026-06-18 09:00---我不想住了，要求全额退款|||SUPPORT---2026-06-18 09:05---根据取消政策，入住前24小时内取消需支付50%违约金")
                    .status(DisputeStatus.REJECTED)
                    .adminNote("根据酒店取消政策，不符合全额退款条件，申请已驳回")
                    .adminId(1L)
                    .resolvedAt(LocalDateTime.now().minusDays(1))
                    .createdAt(LocalDateTime.now().minusDays(4))
                    .build();
            orderDisputeRepository.save(dispute5);

            OrderDispute dispute6 = OrderDispute.builder()
                    .bookingId(bookings.size() > 5 ? bookings.get(5).getId() : bookings.get(2).getId())
                    .applicantId(businesses.get(0).getId())
                    .applicantType(UserType.BUSINESS)
                    .reason("用户未按约定时间入住，且拒绝支付违约金")
                    .evidence("提供了入住协议和用户确认记录")
                    .evidenceImages("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=hotel%20booking%20agreement%20document,https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=user%20confirmation%20message%20screenshot")
                    .communicationRecords("BUSINESS---2026-06-19 10:00---用户预订后未入住，也不支付违约金|||SUPPORT---2026-06-19 10:05---已收到您的申请，我们会联系用户核实")
                    .status(DisputeStatus.PENDING)
                    .createdAt(LocalDateTime.now().minusDays(3))
                    .build();
            orderDisputeRepository.save(dispute6);

            System.out.println("Sample disputes created successfully!");
        }
    }

    private void initializeCities() {
        String[] cityImages = {
            "https://images.unsplash.com/photo-1508804185872-d7badad00f7d?w=800&h=600&fit=crop",
            "https://images.unsplash.com/photo-1548919973-5cef591cdbc9?w=800&h=600&fit=crop",
            "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=600&fit=crop",
            "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800&h=600&fit=crop",
            "https://images.unsplash.com/photo-1582719508461-905c673771fd?w=800&h=600&fit=crop"
        };
        
        List<City> cities = List.of(
            City.builder().name("北京").province("北京市").region("north").isHot(true).hotelCount(5).rating(4.8).description("中国首都，历史文化名城，拥有故宫、长城等世界遗产").image(cityImages[0]).build(),
            City.builder().name("上海").province("上海市").region("east").isHot(true).hotelCount(5).rating(4.9).description("国际化大都市，东方明珠，繁华的商业中心").image(cityImages[1]).build(),
            City.builder().name("广州").province("广东省").region("south").isHot(true).hotelCount(5).rating(4.7).description("岭南文化之都，美食天堂，珠江夜景美不胜收").image(cityImages[2]).build(),
            City.builder().name("深圳").province("广东省").region("south").isHot(true).hotelCount(5).rating(4.8).description("创新之都，科技之城，年轻活力的现代化都市").image(cityImages[0]).build(),
            City.builder().name("杭州").province("浙江省").region("east").isHot(true).hotelCount(5).rating(4.9).description("人间天堂，西湖美景，江南水乡的代表").image(cityImages[1]).build(),
            City.builder().name("成都").province("四川省").region("west").isHot(true).hotelCount(5).rating(4.8).description("天府之国，美食之都，熊猫的故乡").image(cityImages[2]).build(),
            City.builder().name("重庆").province("重庆市").region("west").isHot(true).hotelCount(5).rating(4.7).description("山城雾都，火锅之都，独特的立体城市景观").image(cityImages[0]).build(),
            City.builder().name("三亚").province("海南省").region("south").isHot(true).hotelCount(5).rating(4.9).description("热带海滨度假天堂，阳光沙滩椰林").image(cityImages[3]).build(),
            City.builder().name("厦门").province("福建省").region("south").isHot(true).hotelCount(5).rating(4.8).description("海上花园，鼓浪屿风情，闽南文化").image(cityImages[1]).build(),
            City.builder().name("青岛").province("山东省").region("east").isHot(true).hotelCount(5).rating(4.7).description("海滨之城，啤酒之都，德式建筑风情").image(cityImages[2]).build(),
            City.builder().name("西安").province("陕西省").region("north").isHot(true).hotelCount(5).rating(4.8).description("十三朝古都，兵马俑，丝绸之路起点").image(cityImages[0]).build(),
            City.builder().name("南京").province("江苏省").region("east").isHot(true).hotelCount(5).rating(4.7).description("六朝古都，中山陵，秦淮河畔").image(cityImages[1]).build(),
            City.builder().name("苏州").province("江苏省").region("east").isHot(false).hotelCount(5).rating(4.8).description("园林之城，江南水乡，小桥流水人家").image(cityImages[2]).build(),
            City.builder().name("无锡").province("江苏省").region("east").isHot(false).hotelCount(5).rating(4.6).description("太湖明珠，灵山胜境，江南美景").image(cityImages[0]).build(),
            City.builder().name("宁波").province("浙江省").region("east").isHot(false).hotelCount(5).rating(4.6).description("海港城市，天一阁，海鲜美食").image(cityImages[1]).build(),
            City.builder().name("温州").province("浙江省").region("east").isHot(false).hotelCount(5).rating(4.5).description("民营经济之都，雁荡山风光").image(cityImages[2]).build(),
            City.builder().name("合肥").province("安徽省").region("east").isHot(false).hotelCount(5).rating(4.5).description("科技之城，巢湖风光，徽文化").image(cityImages[0]).build(),
            City.builder().name("福州").province("福建省").region("south").isHot(false).hotelCount(5).rating(4.6).description("榕城，三坊七巷，闽江夜景").image(cityImages[1]).build(),
            City.builder().name("泉州").province("福建省").region("south").isHot(false).hotelCount(5).rating(4.6).description("海上丝绸之路起点，闽南文化").image(cityImages[2]).build(),
            City.builder().name("长沙").province("湖南省").region("central").isHot(true).hotelCount(5).rating(4.7).description("星城，岳麓山，湘菜之都").image(cityImages[0]).build(),
            City.builder().name("武汉").province("湖北省").region("central").isHot(true).hotelCount(5).rating(4.6).description("江城，黄鹤楼，长江大桥").image(cityImages[1]).build(),
            City.builder().name("郑州").province("河南省").region("central").isHot(false).hotelCount(5).rating(4.5).description("中原之都，少林寺，黄河文化").image(cityImages[2]).build(),
            City.builder().name("南昌").province("江西省").region("central").isHot(false).hotelCount(5).rating(4.5).description("英雄城，滕王阁，鄱阳湖").image(cityImages[0]).build(),
            City.builder().name("太原").province("山西省").region("north").isHot(false).hotelCount(5).rating(4.4).description("龙城，晋祠，五台山").image(cityImages[1]).build(),
            City.builder().name("石家庄").province("河北省").region("north").isHot(false).hotelCount(5).rating(4.4).description("燕赵大地，赵州桥，西柏坡").image(cityImages[2]).build(),
            City.builder().name("天津").province("天津市").region("north").isHot(false).hotelCount(5).rating(4.6).description("渤海明珠，五大道，狗不理包子").image(cityImages[0]).build(),
            City.builder().name("沈阳").province("辽宁省").region("north").isHot(false).hotelCount(5).rating(4.5).description("盛京，沈阳故宫，东北文化").image(cityImages[1]).build(),
            City.builder().name("大连").province("辽宁省").region("north").isHot(false).hotelCount(5).rating(4.6).description("浪漫之都，海滨城市，广场文化").image(cityImages[2]).build(),
            City.builder().name("哈尔滨").province("黑龙江省").region("north").isHot(false).hotelCount(5).rating(4.5).description("冰城，冰雪大世界，俄式风情").image(cityImages[0]).build(),
            City.builder().name("长春").province("吉林省").region("north").isHot(false).hotelCount(5).rating(4.4).description("春城，伪满皇宫，长白山").image(cityImages[1]).build(),
            City.builder().name("昆明").province("云南省").region("west").isHot(true).hotelCount(5).rating(4.7).description("春城，滇池，七彩云南").image(cityImages[2]).build(),
            City.builder().name("贵阳").province("贵州省").region("west").isHot(false).hotelCount(5).rating(4.5).description("林城，黄果树瀑布，喀斯特地貌").image(cityImages[0]).build(),
            City.builder().name("南宁").province("广西壮族自治区").region("south").isHot(false).hotelCount(5).rating(4.5).description("绿城，青秀山，壮乡文化").image(cityImages[1]).build(),
            City.builder().name("海口").province("海南省").region("south").isHot(false).hotelCount(5).rating(4.6).description("椰城，海滨风光，热带风情").image(cityImages[3]).build(),
            City.builder().name("珠海").province("广东省").region("south").isHot(false).hotelCount(5).rating(4.7).description("浪漫之城，情侣路，海岛风光").image(cityImages[0]).build(),
            City.builder().name("东莞").province("广东省").region("south").isHot(false).hotelCount(5).rating(4.5).description("制造业之都，可园，岭南文化").image(cityImages[1]).build(),
            City.builder().name("佛山").province("广东省").region("south").isHot(false).hotelCount(5).rating(4.6).description("武术之乡，祖庙，粤菜之乡").image(cityImages[2]).build(),
            City.builder().name("中山").province("广东省").region("south").isHot(false).hotelCount(5).rating(4.5).description("伟人故里，孙文西路，岭南水乡").image(cityImages[0]).build(),
            City.builder().name("惠州").province("广东省").region("south").isHot(false).hotelCount(5).rating(4.6).description("鹅城，西湖，海滨度假").image(cityImages[1]).build(),
            City.builder().name("江门").province("广东省").region("south").isHot(false).hotelCount(5).rating(4.5).description("侨乡，碉楼，小鸟天堂").image(cityImages[2]).build(),
            City.builder().name("绍兴").province("浙江省").region("east").isHot(false).hotelCount(5).rating(4.7).description("水乡古城，鲁迅故里，黄酒之乡").image(cityImages[0]).build(),
            City.builder().name("嘉兴").province("浙江省").region("east").isHot(false).hotelCount(5).rating(4.6).description("红船精神，南湖，粽子之乡").image(cityImages[1]).build(),
            City.builder().name("湖州").province("浙江省").region("east").isHot(false).hotelCount(5).rating(4.5).description("太湖之滨，安吉竹海，丝绸之乡").image(cityImages[2]).build(),
            City.builder().name("金华").province("浙江省").region("east").isHot(false).hotelCount(5).rating(4.6).description("婺城，双龙洞，火腿之乡").image(cityImages[0]).build(),
            City.builder().name("衢州").province("浙江省").region("east").isHot(false).hotelCount(5).rating(4.5).description("南孔圣地，江郎山，开化龙顶").image(cityImages[1]).build(),
            City.builder().name("台州").province("浙江省").region("east").isHot(false).hotelCount(5).rating(4.5).description("山海之城，天台山，神仙居").image(cityImages[2]).build(),
            City.builder().name("丽水").province("浙江省").region("east").isHot(false).hotelCount(5).rating(4.6).description("秀山丽水，古堰画乡，畲族文化").image(cityImages[0]).build(),
            City.builder().name("常州").province("江苏省").region("east").isHot(false).hotelCount(5).rating(4.5).description("龙城，恐龙园，天目湖").image(cityImages[1]).build(),
            City.builder().name("徐州").province("江苏省").region("east").isHot(false).hotelCount(5).rating(4.5).description("彭城，云龙山，汉文化").image(cityImages[2]).build(),
            City.builder().name("南通").province("江苏省").region("east").isHot(false).hotelCount(5).rating(4.5).description("江海门户，狼山，蓝印花布").image(cityImages[0]).build(),
            City.builder().name("扬州").province("江苏省").region("east").isHot(false).hotelCount(5).rating(4.7).description("烟花三月，瘦西湖，淮扬菜").image(cityImages[1]).build(),
            City.builder().name("镇江").province("江苏省").region("east").isHot(false).hotelCount(5).rating(4.5).description("天下第一江山，金山寺，香醋").image(cityImages[2]).build(),
            City.builder().name("盐城").province("江苏省").region("east").isHot(false).hotelCount(5).rating(4.4).description("湿地之都，丹顶鹤，麋鹿").image(cityImages[0]).build(),
            City.builder().name("淮安").province("江苏省").region("east").isHot(false).hotelCount(5).rating(4.5).description("运河之都，周恩来故居，淮扬菜").image(cityImages[1]).build(),
            City.builder().name("连云港").province("江苏省").region("east").isHot(false).hotelCount(5).rating(4.5).description("海港城市，花果山，水晶之都").image(cityImages[2]).build(),
            City.builder().name("宿迁").province("江苏省").region("east").isHot(false).hotelCount(5).rating(4.4).description("项王故里，洪泽湖，洋河酒").image(cityImages[0]).build(),
            City.builder().name("兰州").province("甘肃省").region("west").isHot(false).hotelCount(5).rating(4.5).description("黄河之都，中山桥，拉面之乡").image(cityImages[1]).build(),
            City.builder().name("乌鲁木齐").province("新疆维吾尔自治区").region("west").isHot(false).hotelCount(5).rating(4.5).description("西域之都，天山天池，民族风情").image(cityImages[2]).build(),
            City.builder().name("呼和浩特").province("内蒙古自治区").region("north").isHot(false).hotelCount(5).rating(4.4).description("青城，大草原，蒙古族文化").image(cityImages[0]).build(),
            City.builder().name("银川").province("宁夏回族自治区").region("west").isHot(false).hotelCount(5).rating(4.5).description("塞上江南，西夏王陵，沙湖").image(cityImages[1]).build(),
            City.builder().name("西宁").province("青海省").region("west").isHot(false).hotelCount(5).rating(4.4).description("夏都，青海湖，塔尔寺").image(cityImages[2]).build(),
            City.builder().name("拉萨").province("西藏自治区").region("west").isHot(true).hotelCount(5).rating(4.8).description("日光之城，布达拉宫，雪山圣湖").image(cityImages[4]).build(),
            City.builder().name("香港").province("香港特别行政区").region("hkmt").isHot(true).hotelCount(5).rating(4.8).description("东方之珠，购物天堂，维多利亚港").image(cityImages[1]).build(),
            City.builder().name("澳门").province("澳门特别行政区").region("hkmt").isHot(true).hotelCount(5).rating(4.7).description("东方拉斯维加斯，赌场之都，葡式风情").image(cityImages[2]).build(),
            City.builder().name("台北").province("台湾省").region("hkmt").isHot(true).hotelCount(5).rating(4.7).description("宝岛明珠，故宫博物院，士林夜市").image(cityImages[0]).build()
        );

        cityRepository.saveAll(cities);
        System.out.println("Sample cities created successfully!");
    }
}

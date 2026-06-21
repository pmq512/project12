package com.example.luxurystay;

import com.example.luxurystay.entity.*;
import com.example.luxurystay.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

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
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            initializeUsers();
        }
        if (hotelRepository.count() == 0) {
            initializeHotels();
        }
        if (bookingRepository.count() == 0) {
            initializeBookings();
        }
        if (reviewRepository.count() == 0) {
            initializeReviews();
        }
        if (wishlistRepository.count() == 0) {
            initializeWishlists();
        }
        if (orderDisputeRepository.count() == 0) {
            initializeDisputes();
        }
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
        User business = userRepository.findByUserType(UserType.BUSINESS).get(0);

        // 北京酒店
        Hotel hotel1 = Hotel.builder()
                .name("北京星河湾大酒店")
                .location("北京市朝阳区朝阳北路100号")
                .description("北京星河湾大酒店位于CBD核心区域，是一家五星级豪华商务酒店。酒店拥有精致的客房、顶级的餐饮设施和完善的会议服务，为您提供无与伦比的入住体验。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=luxury%20hotel%20exterior%20Beijing%20modern%20architecture%20golden%20hour&image_size=landscape_16_9")
                .star(5)
                .rating(4.8)
                .price(1288)
                .facilities("免费WiFi,游泳池,健身房,SPA,商务中心,餐厅,酒吧,停车场")
                .reviewCount(156)
                .roomsAvailable(50)
                .businessId(business.getId())
                .depositAmount(200)
                .depositPolicy("预订需支付首晚房费作为定金，入住前7天取消可全额退款")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel1);

        Hotel hotel2 = Hotel.builder()
                .name("北京国贸大酒店")
                .location("北京市朝阳区建国门外大街1号")
                .description("北京国贸大酒店位于CBD中心，是北京最高的酒店之一。酒店拥有俯瞰全城的壮观景观，提供顶级的餐饮和会议服务。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=modern%20skyscraper%20hotel%20Beijing%20CBD%20night%20panorama&image_size=landscape_16_9")
                .star(5)
                .rating(4.7)
                .price(1688)
                .facilities("免费WiFi,游泳池,健身房,SPA,商务中心,餐厅,酒吧,停车场")
                .reviewCount(423)
                .roomsAvailable(280)
                .businessId(business.getId())
                .depositAmount(300)
                .depositPolicy("预订需支付首晚房费作为定金，入住前5天取消可全额退款")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel2);

        Hotel hotel3 = Hotel.builder()
                .name("北京王府半岛酒店")
                .location("北京市东城区王府井金鱼胡同8号")
                .description("北京王府半岛酒店位于王府井商圈核心，融合东方典雅与西方奢华。酒店拥有宽敞的客房和精致的餐饮设施，是京城顶级酒店之一。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=luxury%20hotel%20Beijing%20Wangfujing%20traditional%20Chinese%20architecture&image_size=landscape_16_9")
                .star(5)
                .rating(4.8)
                .price(2288)
                .facilities("免费WiFi,游泳池,健身房,SPA,商务中心,餐厅,酒吧,停车场")
                .reviewCount(189)
                .roomsAvailable(150)
                .businessId(business.getId())
                .depositAmount(400)
                .depositPolicy("预订需支付首晚房费作为定金，入住前3天取消可退款50%")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel3);

        Hotel hotel4 = Hotel.builder()
                .name("北京四季酒店")
                .location("北京市朝阳区亮马桥路48号")
                .description("北京四季酒店位于燕莎商圈，以其优雅的设计和卓越的服务著称。酒店拥有室内泳池、顶级SPA和米其林餐厅，是奢华生活的典范。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Four%20Seasons%20hotel%20Beijing%20elegant%20modern%20design&image_size=landscape_16_9")
                .star(5)
                .rating(4.9)
                .price(2988)
                .facilities("免费WiFi,游泳池,健身房,SPA,商务中心,餐厅,酒吧,停车场")
                .reviewCount(567)
                .roomsAvailable(240)
                .businessId(business.getId())
                .depositAmount(500)
                .depositPolicy("预订需支付首晚房费作为定金，入住前7天取消可全额退款")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel4);

        Hotel hotel5 = Hotel.builder()
                .name("北京丽思卡尔顿酒店")
                .location("北京市朝阳区建国路83号")
                .description("北京丽思卡尔顿酒店位于华贸中心，是全球知名的奢华酒店品牌。酒店以其标志性的服务和精致的设施，为宾客创造难忘的体验。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Ritz%20Carlton%20hotel%20Beijing%20luxury%20elegant%20interior&image_size=landscape_16_9")
                .star(5)
                .rating(4.8)
                .price(2688)
                .facilities("免费WiFi,游泳池,健身房,SPA,商务中心,餐厅,酒吧,停车场")
                .reviewCount(345)
                .roomsAvailable(270)
                .businessId(business.getId())
                .depositAmount(500)
                .depositPolicy("预订需支付首晚房费作为定金，入住前5天取消可全额退款")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel5);

        Hotel hotel6 = Hotel.builder()
                .name("北京瑰丽酒店")
                .location("北京市朝阳区呼家楼京广中心")
                .description("北京瑰丽酒店是一家现代奢华酒店，以其独特的设计理念和个性化服务而闻名。酒店拥有多种房型和丰富的餐饮选择。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Rosewood%20hotel%20Beijing%20modern%20luxury%20design&image_size=landscape_16_9")
                .star(5)
                .rating(4.7)
                .price(2188)
                .facilities("免费WiFi,游泳池,健身房,SPA,商务中心,餐厅,酒吧,停车场")
                .reviewCount(234)
                .roomsAvailable(180)
                .businessId(business.getId())
                .depositAmount(400)
                .depositPolicy("预订需支付首晚房费作为定金，入住前7天取消可全额退款")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel6);

        Hotel hotel7 = Hotel.builder()
                .name("北京华尔道夫酒店")
                .location("北京市东城区金鱼胡同5-15号")
                .description("北京华尔道夫酒店坐落于王府井，是希尔顿旗下的顶级品牌。酒店融合了古典与现代风格，提供优雅的住宿体验。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Waldorf%20Astoria%20hotel%20Beijing%20classic%20elegant&image_size=landscape_16_9")
                .star(5)
                .rating(4.8)
                .price(2488)
                .facilities("免费WiFi,游泳池,健身房,SPA,商务中心,餐厅,酒吧,停车场")
                .reviewCount(312)
                .roomsAvailable(200)
                .businessId(business.getId())
                .depositAmount(400)
                .depositPolicy("预订需支付首晚房费作为定金，入住前3天取消可退款50%")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel7);

        Hotel hotel8 = Hotel.builder()
                .name("北京柏悦酒店")
                .location("北京市朝阳区建国门外大街2号")
                .description("北京柏悦酒店位于银泰中心，是一家高端商务酒店。酒店拥有绝佳的城市景观和顶级的服务设施。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Park%20Hyatt%20hotel%20Beijing%20modern%20luxury%20skyline&image_size=landscape_16_9")
                .star(5)
                .rating(4.7)
                .price(2388)
                .facilities("免费WiFi,游泳池,健身房,SPA,商务中心,餐厅,酒吧,停车场")
                .reviewCount(278)
                .roomsAvailable(220)
                .businessId(business.getId())
                .depositAmount(400)
                .depositPolicy("预订需支付首晚房费作为定金，入住前5天取消可全额退款")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel8);

        Hotel hotel9 = Hotel.builder()
                .name("北京香格里拉大酒店")
                .location("北京市海淀区紫竹院路29号")
                .description("北京香格里拉大酒店位于西三环，是一家融合东西方文化的豪华酒店。酒店拥有美丽的花园和完善的休闲设施。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Shangri%20La%20hotel%20Beijing%20garden%20luxury&image_size=landscape_16_9")
                .star(5)
                .rating(4.6)
                .price(1588)
                .facilities("免费WiFi,游泳池,健身房,SPA,商务中心,餐厅,酒吧,停车场")
                .reviewCount(412)
                .roomsAvailable(300)
                .businessId(business.getId())
                .depositAmount(300)
                .depositPolicy("预订需支付首晚房费作为定金，入住前7天取消可全额退款")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel9);

        Hotel hotel10 = Hotel.builder()
                .name("北京励骏酒店")
                .location("北京市东城区金宝街90号")
                .description("北京励骏酒店位于金宝街，是一家欧式宫廷风格的精品酒店。酒店以其独特的建筑风格和个性化服务著称。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=European%20style%20boutique%20hotel%20Beijing%20palace&image_size=landscape_16_9")
                .star(5)
                .rating(4.5)
                .price(1888)
                .facilities("免费WiFi,健身房,SPA,餐厅,酒吧,停车场")
                .reviewCount(156)
                .roomsAvailable(100)
                .businessId(business.getId())
                .depositAmount(350)
                .depositPolicy("预订需支付首晚房费作为定金，入住前5天取消可全额退款")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel10);

        // 上海酒店
        Hotel hotel11 = Hotel.builder()
                .name("上海外滩华尔道夫酒店")
                .location("上海市黄浦区中山东一路2号")
                .description("上海外滩华尔道夫酒店坐落于外滩核心位置，是上海最具传奇色彩的豪华酒店之一。酒店融合了古典优雅与现代奢华，为宾客提供世界级的服务体验。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=luxury%20hotel%20Shanghai%20Bund%20classic%20architecture%20night%20view&image_size=landscape_16_9")
                .star(5)
                .rating(4.9)
                .price(2588)
                .facilities("免费WiFi,游泳池,健身房,SPA,商务中心,餐厅,酒吧,停车场,水疗中心")
                .reviewCount(234)
                .roomsAvailable(120)
                .businessId(business.getId())
                .depositAmount(500)
                .depositPolicy("预订需支付首晚房费作为定金，入住前3天取消可退款50%")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel11);

        Hotel hotel12 = Hotel.builder()
                .name("上海半岛酒店")
                .location("上海市黄浦区中山东一路32号")
                .description("上海半岛酒店位于外滩，是半岛集团在中国大陆的旗舰酒店。酒店以其典雅的设计和卓越的服务，成为上海奢华酒店的标杆。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Peninsula%20hotel%20Shanghai%20Bund%20classic%20luxury&image_size=landscape_16_9")
                .star(5)
                .rating(4.9)
                .price(3288)
                .facilities("免费WiFi,游泳池,健身房,SPA,商务中心,餐厅,酒吧,停车场")
                .reviewCount(456)
                .roomsAvailable(230)
                .businessId(business.getId())
                .depositAmount(600)
                .depositPolicy("预订需支付首晚房费作为定金，入住前3天取消可退款50%")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel12);

        Hotel hotel13 = Hotel.builder()
                .name("上海四季酒店")
                .location("上海市静安区威海路500号")
                .description("上海四季酒店位于市中心，是一家兼具商务与休闲功能的豪华酒店。酒店拥有米其林星级餐厅和顶级SPA设施。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Four%20Seasons%20hotel%20Shanghai%20modern%20luxury&image_size=landscape_16_9")
                .star(5)
                .rating(4.8)
                .price(2888)
                .facilities("免费WiFi,游泳池,健身房,SPA,商务中心,餐厅,酒吧,停车场")
                .reviewCount(321)
                .roomsAvailable(250)
                .businessId(business.getId())
                .depositAmount(500)
                .depositPolicy("预订需支付首晚房费作为定金，入住前5天取消可全额退款")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel13);

        Hotel hotel14 = Hotel.builder()
                .name("上海浦东丽思卡尔顿酒店")
                .location("上海市浦东新区世纪大道8号")
                .description("上海浦东丽思卡尔顿酒店位于陆家嘴金融中心，是上海最高的酒店之一。酒店拥有俯瞰黄浦江的壮丽景观。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Ritz%20Carlton%20hotel%20Shanghai%20Pudong%20skyline%20view&image_size=landscape_16_9")
                .star(5)
                .rating(4.8)
                .price(3188)
                .facilities("免费WiFi,游泳池,健身房,SPA,商务中心,餐厅,酒吧,停车场")
                .reviewCount(567)
                .roomsAvailable(320)
                .businessId(business.getId())
                .depositAmount(600)
                .depositPolicy("预订需支付首晚房费作为定金，入住前7天取消可全额退款")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel14);

        Hotel hotel15 = Hotel.builder()
                .name("上海艾迪逊酒店")
                .location("上海市黄浦区南京东路199号")
                .description("上海艾迪逊酒店位于南京路步行街，是一家融合现代设计与历史建筑的时尚酒店。酒店拥有屋顶酒吧和精致餐厅。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Edition%20hotel%20Shanghai%20modern%20design%20urban&image_size=landscape_16_9")
                .star(5)
                .rating(4.7)
                .price(2588)
                .facilities("免费WiFi,游泳池,健身房,酒吧,餐厅,停车场")
                .reviewCount(234)
                .roomsAvailable(180)
                .businessId(business.getId())
                .depositAmount(400)
                .depositPolicy("预订需支付首晚房费作为定金，入住前5天取消可全额退款")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel15);

        Hotel hotel16 = Hotel.builder()
                .name("上海璞丽酒店")
                .location("上海市静安区常德路1号")
                .description("上海璞丽酒店是一家精品奢华酒店，以其独特的设计风格和私密性著称。酒店位于静安寺商圈，是都市中的静谧绿洲。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=PuLi%20hotel%20Shanghai%20boutique%20luxury%20spa&image_size=landscape_16_9")
                .star(5)
                .rating(4.8)
                .price(2788)
                .facilities("免费WiFi,游泳池,SPA,餐厅,酒吧,停车场")
                .reviewCount(189)
                .roomsAvailable(150)
                .businessId(business.getId())
                .depositAmount(450)
                .depositPolicy("预订需支付首晚房费作为定金，入住前5天取消可全额退款")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel16);

        Hotel hotel17 = Hotel.builder()
                .name("上海静安香格里拉大酒店")
                .location("上海市静安区延安中路1218号")
                .description("上海静安香格里拉大酒店位于静安核心商圈，是一家集商务与休闲于一体的高端酒店。酒店拥有完善的会议设施和多个风味餐厅。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Shangri%20La%20hotel%20Shanghai%20Jingan%20modern%20tower&image_size=landscape_16_9")
                .star(5)
                .rating(4.7)
                .price(1888)
                .facilities("免费WiFi,游泳池,健身房,SPA,商务中心,餐厅,酒吧,停车场")
                .reviewCount(345)
                .roomsAvailable(280)
                .businessId(business.getId())
                .depositAmount(350)
                .depositPolicy("预订需支付首晚房费作为定金，入住前7天取消可全额退款")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel17);

        Hotel hotel18 = Hotel.builder()
                .name("上海外滩W酒店")
                .location("上海市黄浦区外滩")
                .description("上海外滩W酒店是一家时尚潮流酒店，位于外滩黄金地段。酒店以其前卫的设计和活力四射的氛围吸引年轻旅客。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=W%20hotel%20Shanghai%20Bund%20modern%20trendy%20nightlife&image_size=landscape_16_9")
                .star(5)
                .rating(4.6)
                .price(2288)
                .facilities("免费WiFi,游泳池,健身房,酒吧,餐厅,停车场")
                .reviewCount(278)
                .roomsAvailable(200)
                .businessId(business.getId())
                .depositAmount(400)
                .depositPolicy("预订需支付首晚房费作为定金，入住前3天取消可退款50%")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel18);

        Hotel hotel19 = Hotel.builder()
                .name("上海外滩悦榕庄")
                .location("上海市虹口区公平路36号")
                .description("上海外滩悦榕庄是一家精品度假酒店，拥有绝佳的江景视野。酒店以其私密性和顶级SPA服务著称。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Banyan%20Tree%20hotel%20Shanghai%20river%20view%20spa%20luxury&image_size=landscape_16_9")
                .star(5)
                .rating(4.8)
                .price(2988)
                .facilities("免费WiFi,游泳池,SPA,餐厅,酒吧,停车场")
                .reviewCount(167)
                .roomsAvailable(120)
                .businessId(business.getId())
                .depositAmount(500)
                .depositPolicy("预订需支付首晚房费作为定金，入住前5天取消可全额退款")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel19);

        Hotel hotel20 = Hotel.builder()
                .name("上海宝格丽酒店")
                .location("上海市静安区河南北路88号")
                .description("上海宝格丽酒店是一家超奢华精品酒店，融合意大利风情与上海韵味。酒店以其独特的设计和顶级服务著称。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Bulgari%20hotel%20Shanghai%20Italian%20luxury%20design&image_size=landscape_16_9")
                .star(5)
                .rating(4.9)
                .price(4588)
                .facilities("免费WiFi,游泳池,SPA,餐厅,酒吧,停车场")
                .reviewCount(123)
                .roomsAvailable(80)
                .businessId(business.getId())
                .depositAmount(800)
                .depositPolicy("预订需支付首晚房费作为定金，入住前7天取消可全额退款")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel20);

        // 三亚酒店
        Hotel hotel21 = Hotel.builder()
                .name("三亚海棠湾洲际度假酒店")
                .location("海南省三亚市海棠区海棠北路88号")
                .description("三亚海棠湾洲际度假酒店位于美丽的海棠湾畔，拥有私人海滩和热带花园。酒店提供水上乐园、温泉、SPA等丰富的休闲设施，是您度假的完美选择。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=tropical%20luxury%20resort%20Sanya%20beach%20palm%20trees%20ocean%20view&image_size=landscape_16_9")
                .star(5)
                .rating(4.7)
                .price(1888)
                .facilities("免费WiFi,游泳池,私人海滩,水上乐园,温泉,SPA,餐厅,酒吧,停车场")
                .reviewCount(312)
                .roomsAvailable(200)
                .businessId(business.getId())
                .depositAmount(300)
                .depositPolicy("预订需支付首晚房费作为定金，入住前5天取消可全额退款")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel21);

        Hotel hotel22 = Hotel.builder()
                .name("三亚亚龙湾丽思卡尔顿酒店")
                .location("海南省三亚市亚龙湾国家旅游度假区")
                .description("三亚亚龙湾丽思卡尔顿酒店坐落于亚龙湾最美海滩，是一家热带风情的奢华度假酒店。酒店拥有高尔夫球场和顶级SPA。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Ritz%20Carlton%20resort%20Sanya%20Yalong%20Bay%20tropical%20beach&image_size=landscape_16_9")
                .star(5)
                .rating(4.8)
                .price(2588)
                .facilities("免费WiFi,游泳池,私人海滩,高尔夫,SPA,餐厅,酒吧,停车场")
                .reviewCount(456)
                .roomsAvailable(250)
                .businessId(business.getId())
                .depositAmount(450)
                .depositPolicy("预订需支付首晚房费作为定金，入住前7天取消可全额退款")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel22);

        Hotel hotel23 = Hotel.builder()
                .name("三亚亚特兰蒂斯酒店")
                .location("海南省三亚市海棠区海棠北路")
                .description("三亚亚特兰蒂斯酒店是一家海洋主题的超大型度假酒店，拥有壮观的水族馆和水上乐园。是亲子度假的首选。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Atlantis%20hotel%20Sanya%20aquarium%20water%20park%20ocean&image_size=landscape_16_9")
                .star(5)
                .rating(4.6)
                .price(2888)
                .facilities("免费WiFi,游泳池,私人海滩,水上乐园,水族馆,SPA,餐厅,酒吧,停车场")
                .reviewCount(789)
                .roomsAvailable(500)
                .businessId(business.getId())
                .depositAmount(500)
                .depositPolicy("预订需支付首晚房费作为定金，入住前5天取消可全额退款")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel23);

        Hotel hotel24 = Hotel.builder()
                .name("三亚艾迪逊酒店")
                .location("海南省三亚市海棠区海棠北路100号")
                .description("三亚艾迪逊酒店是一家时尚精品度假酒店，以其简约现代的设计和私密海滩著称。酒店拥有多个特色餐厅和酒吧。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Edition%20resort%20Sanya%20modern%20beach%20tropical&image_size=landscape_16_9")
                .star(5)
                .rating(4.7)
                .price(2388)
                .facilities("免费WiFi,游泳池,私人海滩,SPA,餐厅,酒吧,停车场")
                .reviewCount(234)
                .roomsAvailable(180)
                .businessId(business.getId())
                .depositAmount(400)
                .depositPolicy("预订需支付首晚房费作为定金，入住前5天取消可全额退款")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel24);

        Hotel hotel25 = Hotel.builder()
                .name("三亚保利瑰丽酒店")
                .location("海南省三亚市海棠区海棠湾")
                .description("三亚保利瑰丽酒店是一家高端度假酒店，拥有壮丽的海景和热带花园。酒店以其私密性和个性化服务著称。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Rosewood%20resort%20Sanya%20beach%20villa%20tropical&image_size=landscape_16_9")
                .star(5)
                .rating(4.8)
                .price(3288)
                .facilities("免费WiFi,游泳池,私人海滩,SPA,餐厅,酒吧,停车场")
                .reviewCount(178)
                .roomsAvailable(150)
                .businessId(business.getId())
                .depositAmount(550)
                .depositPolicy("预订需支付首晚房费作为定金，入住前7天取消可全额退款")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel25);

        Hotel hotel26 = Hotel.builder()
                .name("三亚万豪度假酒店")
                .location("海南省三亚市亚龙湾国家旅游度假区")
                .description("三亚万豪度假酒店位于亚龙湾中心位置，拥有绵延的私人海滩。酒店提供丰富的水上活动和儿童俱乐部。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Marriott%20resort%20Sanya%20beach%20pool%20tropical&image_size=landscape_16_9")
                .star(5)
                .rating(4.6)
                .price(1688)
                .facilities("免费WiFi,游泳池,私人海滩,儿童俱乐部,SPA,餐厅,酒吧,停车场")
                .reviewCount(567)
                .roomsAvailable(350)
                .businessId(business.getId())
                .depositAmount(300)
                .depositPolicy("预订需支付首晚房费作为定金，入住前5天取消可全额退款")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel26);

        Hotel hotel27 = Hotel.builder()
                .name("三亚希尔顿逸林度假酒店")
                .location("海南省三亚市亚龙湾国家旅游度假区")
                .description("三亚希尔顿逸林度假酒店坐落于亚龙湾，是一家适合全家度假的豪华酒店。酒店拥有热带花园和多个泳池。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Hilton%20resort%20Sanya%20tropical%20garden%20pool&image_size=landscape_16_9")
                .star(5)
                .rating(4.5)
                .price(1488)
                .facilities("免费WiFi,游泳池,私人海滩,儿童俱乐部,SPA,餐厅,酒吧,停车场")
                .reviewCount(423)
                .roomsAvailable(300)
                .businessId(business.getId())
                .depositAmount(250)
                .depositPolicy("预订需支付首晚房费作为定金，入住前7天取消可全额退款")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel27);

        Hotel hotel28 = Hotel.builder()
                .name("三亚海棠湾康莱德酒店")
                .location("海南省三亚市海棠区海棠北路")
                .description("三亚海棠湾康莱德酒店是一家精品奢华度假酒店，拥有独立泳池别墅。酒店以其私密性和高端服务著称。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Conrad%20resort%20Sanya%20villa%20pool%20luxury&image_size=landscape_16_9")
                .star(5)
                .rating(4.7)
                .price(2988)
                .facilities("免费WiFi,私人泳池,私人海滩,SPA,餐厅,酒吧,停车场")
                .reviewCount(189)
                .roomsAvailable(100)
                .businessId(business.getId())
                .depositAmount(500)
                .depositPolicy("预订需支付首晚房费作为定金，入住前7天取消可全额退款")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel28);

        Hotel hotel29 = Hotel.builder()
                .name("三亚海棠湾红树林度假酒店")
                .location("海南省三亚市海棠区海棠北路")
                .description("三亚海棠湾红树林度假酒店是一家大型度假综合体，拥有丰富的餐饮和娱乐设施。酒店适合各类旅客。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Mangrove%20resort%20Sanya%20beach%20tower%20luxury&image_size=landscape_16_9")
                .star(5)
                .rating(4.5)
                .price(1588)
                .facilities("免费WiFi,游泳池,私人海滩,儿童乐园,SPA,餐厅,酒吧,停车场")
                .reviewCount(345)
                .roomsAvailable(400)
                .businessId(business.getId())
                .depositAmount(280)
                .depositPolicy("预订需支付首晚房费作为定金，入住前5天取消可全额退款")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel29);

        Hotel hotel30 = Hotel.builder()
                .name("三亚天房洲际度假酒店")
                .location("海南省三亚市海棠区海棠湾")
                .description("三亚天房洲际度假酒店位于海棠湾，拥有绝美的海景和热带花园。酒店提供丰富的亲子活动和休闲设施。")
                .image("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=InterContinental%20resort%20Sanya%20beach%20family%20pool&image_size=landscape_16_9")
                .star(5)
                .rating(4.6)
                .price(1788)
                .facilities("免费WiFi,游泳池,私人海滩,儿童俱乐部,SPA,餐厅,酒吧,停车场")
                .reviewCount(267)
                .roomsAvailable(280)
                .businessId(business.getId())
                .depositAmount(320)
                .depositPolicy("预订需支付首晚房费作为定金，入住前5天取消可全额退款")
                .noShowPolicy("未入住且未提前取消，定金不予退款")
                .commissionRate(10)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        hotelRepository.save(hotel30);

        System.out.println("Sample hotels created successfully!");
    }

    private void initializeBookings() {
        User user = userRepository.findByUserType(UserType.USER).get(0);
        User user2 = userRepository.findByUserType(UserType.USER).get(1);
        User user3 = userRepository.findByUserType(UserType.USER).get(2);
        List<Hotel> hotels = hotelRepository.findAll();

        if (hotels.size() >= 5) {
            // 已确认的订单
            Booking booking1 = Booking.builder()
                    .userId(user.getId())
                    .hotelId(hotels.get(0).getId())
                    .checkIn(LocalDate.now().plusDays(7))
                    .checkOut(LocalDate.now().plusDays(10))
                    .adults(2)
                    .children(0)
                    .roomType("标准间")
                    .guests(2)
                    .guestName(user.getName())
                    .phone(user.getPhone())
                    .totalPrice(hotels.get(0).getPrice() * 3)
                    .status(BookingStatus.CONFIRMED)
                    .createdAt(LocalDateTime.now())
                    .build();
            bookingRepository.save(booking1);

            // 待确认的订单
            Booking booking2 = Booking.builder()
                    .userId(user.getId())
                    .hotelId(hotels.get(1).getId())
                    .checkIn(LocalDate.now().plusDays(14))
                    .checkOut(LocalDate.now().plusDays(16))
                    .adults(2)
                    .children(1)
                    .roomType("家庭房")
                    .guests(3)
                    .guestName(user.getName())
                    .phone(user.getPhone())
                    .totalPrice(hotels.get(1).getPrice() * 2)
                    .status(BookingStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .build();
            bookingRepository.save(booking2);

            // 已完成的订单
            Booking booking3 = Booking.builder()
                    .userId(user.getId())
                    .hotelId(hotels.get(2).getId())
                    .checkIn(LocalDate.now().minusDays(10))
                    .checkOut(LocalDate.now().minusDays(7))
                    .adults(2)
                    .children(0)
                    .roomType("豪华间")
                    .guests(2)
                    .guestName(user.getName())
                    .phone(user.getPhone())
                    .totalPrice(hotels.get(2).getPrice() * 3)
                    .status(BookingStatus.COMPLETED)
                    .createdAt(LocalDateTime.now().minusDays(15))
                    .build();
            bookingRepository.save(booking3);

            // 更多订单
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
                    .createdAt(LocalDateTime.now().minusHours(5))
                    .build();
            bookingRepository.save(booking7);

            Booking booking8 = Booking.builder()
                    .userId(user.getId())
                    .hotelId(hotels.get(6).getId())
                    .checkIn(LocalDate.now().minusDays(5))
                    .checkOut(LocalDate.now().minusDays(3))
                    .adults(1)
                    .children(0)
                    .roomType("大床房")
                    .guests(1)
                    .guestName(user.getName())
                    .phone(user.getPhone())
                    .totalPrice(hotels.get(6).getPrice() * 2)
                    .status(BookingStatus.USER_CANCELLED)
                    .createdAt(LocalDateTime.now().minusDays(10))
                    .build();
            bookingRepository.save(booking8);
        }

        System.out.println("Sample bookings created successfully!");
    }

    private void initializeReviews() {
        User user = userRepository.findByUserType(UserType.USER).get(0);
        User user2 = userRepository.findByUserType(UserType.USER).get(1);
        User user3 = userRepository.findByUserType(UserType.USER).get(2);
        List<Hotel> hotels = hotelRepository.findAll();

        if (hotels.size() >= 6) {
            // 已通过的评论
            Review review1 = Review.builder()
                    .userId(user.getId())
                    .userName(user.getName())
                    .hotelId(hotels.get(0).getId())
                    .rating(5)
                    .title("非常棒的酒店体验")
                    .content("非常棒的酒店！服务一流，房间宽敞干净，早餐丰富。位置也很方便，下次还会再来！")
                    .status(ReviewStatus.APPROVED)
                    .createdAt(LocalDateTime.now().minusDays(5))
                    .build();
            reviewRepository.save(review1);

            Review review2 = Review.builder()
                    .userId(user.getId())
                    .userName(user.getName())
                    .hotelId(hotels.get(1).getId())
                    .rating(4)
                    .title("环境很好的酒店")
                    .content("酒店环境很好，设施齐全，但服务响应速度有待提高。总体来说是一次愉快的入住体验。")
                    .status(ReviewStatus.APPROVED)
                    .createdAt(LocalDateTime.now().minusDays(3))
                    .build();
            reviewRepository.save(review2);

            Review review3 = Review.builder()
                    .userId(user.getId())
                    .userName(user.getName())
                    .hotelId(hotels.get(2).getId())
                    .rating(5)
                    .title("度假首选")
                    .content("度假首选！私人海滩很美，水上乐园孩子们玩得很开心。SPA也很专业，强烈推荐！")
                    .status(ReviewStatus.APPROVED)
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .build();
            reviewRepository.save(review3);

            // 待审核的评论
            Review review4 = Review.builder()
                    .userId(user2.getId())
                    .userName(user2.getName())
                    .hotelId(hotels.get(3).getId())
                    .rating(4)
                    .title("商务出行首选")
                    .content("商务出行首选！地理位置优越，交通便利。会议室设施先进，服务周到。")
                    .status(ReviewStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .build();
            reviewRepository.save(review4);

            Review review5 = Review.builder()
                    .userId(user2.getId())
                    .userName(user2.getName())
                    .hotelId(hotels.get(4).getId())
                    .rating(5)
                    .title("完美的周末度假")
                    .content("完美的周末度假！酒店服务无可挑剔，房间视野很好，能看到城市全景。")
                    .status(ReviewStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .build();
            reviewRepository.save(review5);

            Review review6 = Review.builder()
                    .userId(user.getId())
                    .userName(user.getName())
                    .hotelId(hotels.get(5).getId())
                    .rating(3)
                    .title("体验一般")
                    .content("整体体验一般，房间隔音效果不太好，早餐种类较少。希望能够改进。")
                    .status(ReviewStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .build();
            reviewRepository.save(review6);

            // 更多待审核评论
            Review review7 = Review.builder()
                    .userId(user3.getId())
                    .userName(user3.getName())
                    .hotelId(hotels.get(6).getId())
                    .rating(5)
                    .title("超五星体验")
                    .content("服务超五星！从入住到退房，每个环节都非常专业。房间设施一流，早餐丰富多样。")
                    .status(ReviewStatus.PENDING)
                    .createdAt(LocalDateTime.now().minusHours(2))
                    .build();
            reviewRepository.save(review7);

            Review review8 = Review.builder()
                    .userId(user2.getId())
                    .userName(user2.getName())
                    .hotelId(hotels.get(7).getId())
                    .rating(4)
                    .title("性价比很高")
                    .content("性价比很高的酒店，位置好，服务好，价格合理。下次还会选择这里。")
                    .status(ReviewStatus.PENDING)
                    .createdAt(LocalDateTime.now().minusHours(4))
                    .build();
            reviewRepository.save(review8);

            Review review9 = Review.builder()
                    .userId(user3.getId())
                    .userName(user3.getName())
                    .hotelId(hotels.get(8).getId())
                    .rating(5)
                    .title("完美的蜜月之旅")
                    .content("完美的蜜月之旅！酒店为我们准备了特别的惊喜，服务贴心周到，房间浪漫温馨。")
                    .status(ReviewStatus.PENDING)
                    .createdAt(LocalDateTime.now().minusHours(6))
                    .build();
            reviewRepository.save(review9);

            Review review10 = Review.builder()
                    .userId(user.getId())
                    .userName(user.getName())
                    .hotelId(hotels.get(9).getId())
                    .rating(4)
                    .title("不错的商务酒店")
                    .content("不错的商务酒店，设施齐全，服务专业。早餐种类丰富，位置便利。")
                    .status(ReviewStatus.PENDING)
                    .createdAt(LocalDateTime.now().minusHours(8))
                    .build();
            reviewRepository.save(review10);

            // 已驳回的评论
            Review review11 = Review.builder()
                    .userId(user3.getId())
                    .userName(user3.getName())
                    .hotelId(hotels.get(10).getId())
                    .rating(1)
                    .title("非常失望")
                    .content("非常失望！房间脏乱差，服务态度恶劣，完全不符合五星级酒店的标准。")
                    .status(ReviewStatus.REJECTED)
                    .createdAt(LocalDateTime.now().minusDays(2))
                    .build();
            reviewRepository.save(review11);
        }

        System.out.println("Sample reviews created successfully!");
    }

    private void initializeWishlists() {
        User user = userRepository.findByUserType(UserType.USER).get(0);
        List<Hotel> hotels = hotelRepository.findAll();

        if (hotels.size() >= 5) {
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
        
        if (bookings.size() >= 3 && users.size() >= 3) {
            // 待处理纠纷 - 用户申请退款
            OrderDispute dispute1 = OrderDispute.builder()
                    .bookingId(bookings.get(0).getId())
                    .applicantId(users.get(0).getId())
                    .applicantType(UserType.USER)
                    .reason("酒店设施与描述不符，房间空调无法正常使用，要求全额退款")
                    .evidence("提供了房间照片和与客服沟通记录")
                    .status(DisputeStatus.PENDING)
                    .createdAt(LocalDateTime.now().minusDays(2))
                    .build();
            orderDisputeRepository.save(dispute1);

            // 待处理纠纷 - 商家申诉
            OrderDispute dispute2 = OrderDispute.builder()
                    .bookingId(bookings.get(1).getId())
                    .applicantId(users.get(1).getId())
                    .applicantType(UserType.USER)
                    .reason("用户未按约定时间入住，且拒绝支付违约金")
                    .evidence("提供了入住协议和用户确认记录")
                    .status(DisputeStatus.PENDING)
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .build();
            orderDisputeRepository.save(dispute2);

            // 处理中纠纷
            OrderDispute dispute3 = OrderDispute.builder()
                    .bookingId(bookings.get(2).getId())
                    .applicantId(users.get(2).getId())
                    .applicantType(UserType.USER)
                    .reason("预订后价格下降，要求退还差价")
                    .evidence("提供了价格截图和预订确认邮件")
                    .status(DisputeStatus.PROCESSING)
                    .adminNote("已联系酒店核实情况，等待回复")
                    .adminId(1L)
                    .createdAt(LocalDateTime.now().minusDays(5))
                    .build();
            orderDisputeRepository.save(dispute3);

            // 已解决纠纷
            OrderDispute dispute4 = OrderDispute.builder()
                    .bookingId(bookings.size() > 3 ? bookings.get(3).getId() : bookings.get(0).getId())
                    .applicantId(users.get(0).getId())
                    .applicantType(UserType.USER)
                    .reason("酒店卫生条件差，床单有污渍")
                    .evidence("提供了现场照片")
                    .status(DisputeStatus.RESOLVED)
                    .adminNote("经核实，酒店已承认问题并同意退款50%")
                    .adminId(1L)
                    .resolvedAt(LocalDateTime.now().minusDays(3))
                    .createdAt(LocalDateTime.now().minusDays(7))
                    .build();
            orderDisputeRepository.save(dispute4);

            // 已驳回纠纷
            OrderDispute dispute5 = OrderDispute.builder()
                    .bookingId(bookings.size() > 4 ? bookings.get(4).getId() : bookings.get(1).getId())
                    .applicantId(users.get(1).getId())
                    .applicantType(UserType.USER)
                    .reason("要求无理由全额退款")
                    .evidence("无有效证据")
                    .status(DisputeStatus.REJECTED)
                    .adminNote("根据酒店取消政策，不符合全额退款条件，申请已驳回")
                    .adminId(1L)
                    .resolvedAt(LocalDateTime.now().minusDays(1))
                    .createdAt(LocalDateTime.now().minusDays(4))
                    .build();
            orderDisputeRepository.save(dispute5);

            System.out.println("Sample disputes created successfully!");
        }
    }
}
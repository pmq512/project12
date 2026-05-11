package com.anime;

import com.anime.entity.Anime;
import com.anime.entity.Category;
import com.anime.entity.User;
import com.anime.service.AnimeService;
import com.anime.service.CategoryService;
import com.anime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private AnimeService animeService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        // 添加示例分类数据
        if (categoryService.getAllCategories().isEmpty()) {
            Category category1 = new Category();
            category1.setName("热血");
            category1.setDescription("充满激情和战斗的动漫");
            categoryService.saveCategory(category1);

            Category category2 = new Category();
            category2.setName("冒险");
            category2.setDescription("探索未知世界的动漫");
            categoryService.saveCategory(category2);

            Category category3 = new Category();
            category3.setName("科幻");
            category3.setDescription("未来科技和宇宙题材的动漫");
            categoryService.saveCategory(category3);

            Category category4 = new Category();
            category4.setName("奇幻");
            category4.setDescription("魔法、超自然现象的动漫");
            categoryService.saveCategory(category4);

            Category category5 = new Category();
            category5.setName("校园");
            category5.setDescription("以学校生活为背景的动漫");
            categoryService.saveCategory(category5);

            Category category6 = new Category();
            category6.setName("恋爱");
            category6.setDescription("关于爱情故事的动漫");
            categoryService.saveCategory(category6);

            Category category7 = new Category();
            category7.setName("喜剧");
            category7.setDescription("以幽默搞笑为主题的动漫");
            categoryService.saveCategory(category7);

            Category category8 = new Category();
            category8.setName("悬疑");
            category8.setDescription("充满谜团和推理的动漫");
            categoryService.saveCategory(category8);

            Category category9 = new Category();
            category9.setName("恐怖");
            category9.setDescription("惊悚、恐怖题材的动漫");
            categoryService.saveCategory(category9);

            Category category10 = new Category();
            category10.setName("魔法");
            category10.setDescription("以魔法为主题的动漫");
            categoryService.saveCategory(category10);

            Category category11 = new Category();
            category11.setName("机甲");
            category11.setDescription("机器人、机甲战斗的动漫");
            categoryService.saveCategory(category11);

            Category category12 = new Category();
            category12.setName("推理");
            category12.setDescription("侦探、推理题材的动漫");
            categoryService.saveCategory(category12);

            Category category13 = new Category();
            category13.setName("历史");
            category13.setDescription("以历史事件为背景的动漫");
            categoryService.saveCategory(category13);

            Category category14 = new Category();
            category14.setName("体育");
            category14.setDescription("体育竞技题材的动漫");
            categoryService.saveCategory(category14);

            Category category15 = new Category();
            category15.setName("音乐");
            category15.setDescription("以音乐为主题的动漫");
            categoryService.saveCategory(category15);
        }

        // 添加示例用户数据
        if (!userService.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin");
            admin.setEmail("admin@example.com");
            admin.setRole("ADMIN");
            userService.saveUser(admin);
        }

        if (!userService.existsByUsername("user")) {
            User user = new User();
            user.setUsername("user");
            user.setPassword("user");
            user.setEmail("user@example.com");
            user.setRole("USER");
            userService.saveUser(user);
        }

        // 添加示例动漫数据
        if (animeService.getAllAnime().isEmpty()) {
            Category category1 = categoryService.getCategoryByName("热血");
            Category category2 = categoryService.getCategoryByName("冒险");
            Category category3 = categoryService.getCategoryByName("科幻");
            Category category4 = categoryService.getCategoryByName("奇幻");
            Category category5 = categoryService.getCategoryByName("校园");
            Category category6 = categoryService.getCategoryByName("恋爱");
            Category category7 = categoryService.getCategoryByName("喜剧");
            Category category8 = categoryService.getCategoryByName("悬疑");
            Category category9 = categoryService.getCategoryByName("恐怖");
            Category category10 = categoryService.getCategoryByName("魔法");
            Category category11 = categoryService.getCategoryByName("机甲");
            Category category12 = categoryService.getCategoryByName("推理");
            Category category13 = categoryService.getCategoryByName("历史");
            Category category14 = categoryService.getCategoryByName("体育");
            Category category15 = categoryService.getCategoryByName("音乐");

            // 热血类
            Anime anime1 = new Anime();
            anime1.setTitle("进击的巨人");
            anime1.setDescription("故事发生在一个被巨大围墙包围的世界，人类为了生存与巨人战斗。");
            anime1.setCoverImage("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Attack%20on%20Titan%20anime%20cover%20image&image_size=square_hd");
            anime1.setVideoUrl("https://www.youtube.com/embed/1g5dRc3sEFU");
            anime1.setCategory(category1);
            animeService.saveAnime(anime1);

            Anime anime2 = new Anime();
            anime2.setTitle("鬼灭之刃");
            anime2.setDescription("炭治郎为了让变成鬼的妹妹祢豆子变回人类，踏上了斩鬼之旅。");
            anime2.setCoverImage("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Demon%20Slayer%20anime%20cover%20image&image_size=square_hd");
            anime2.setVideoUrl("https://www.youtube.com/embed/CHt9P8mQZ8M");
            anime2.setCategory(category1);
            animeService.saveAnime(anime2);

            // 冒险类
            Anime anime3 = new Anime();
            anime3.setTitle("海贼王");
            anime3.setDescription("蒙奇·D·路飞为了成为海贼王，与伙伴们一起踏上伟大的航道。");
            anime3.setCoverImage("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=One%20Piece%20anime%20cover%20image&image_size=square_hd");
            anime3.setVideoUrl("https://www.youtube.com/embed/Z7l8z66G9Ko");
            anime3.setCategory(category2);
            animeService.saveAnime(anime3);

            Anime anime4 = new Anime();
            anime4.setTitle("航海王");
            anime4.setDescription("路飞和他的伙伴们在伟大航路上的冒险故事。");
            anime4.setCoverImage("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=One%20Piece%20straw%20hat%20pirates%20anime%20cover&image_size=square_hd");
            anime4.setVideoUrl("https://www.youtube.com/embed/Z7l8z66G9Ko");
            anime4.setCategory(category2);
            animeService.saveAnime(anime4);

            // 科幻类
            Anime anime5 = new Anime();
            anime5.setTitle("进击的巨人 最终季");
            anime5.setDescription("人类与巨人的最终决战，揭露世界的真相。");
            anime5.setCoverImage("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Attack%20on%20Titan%20final%20season%20anime%20cover&image_size=square_hd");
            anime5.setVideoUrl("https://www.youtube.com/embed/1g5dRc3sEFU");
            anime5.setCategory(category3);
            animeService.saveAnime(anime5);

            Anime anime6 = new Anime();
            anime6.setTitle("高达SEED");
            anime6.setDescription("未来世界，人类为了生存而进行的机甲战争。");
            anime6.setCoverImage("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Gundam%20SEED%20anime%20cover%20image&image_size=square_hd");
            anime6.setVideoUrl("https://www.youtube.com/embed/dQw4w9WgXcQ");
            anime6.setCategory(category3);
            animeService.saveAnime(anime6);

            // 奇幻类
            Anime anime7 = new Anime();
            anime7.setTitle("全职猎人");
            anime7.setDescription("小杰为了寻找父亲，踏上了猎人执照的考试之旅。");
            anime7.setCoverImage("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Hunter%20x%20Hunter%20anime%20cover%20image&image_size=square_hd");
            anime7.setVideoUrl("https://www.youtube.com/embed/dQw4w9WgXcQ");
            anime7.setCategory(category4);
            animeService.saveAnime(anime7);

            // 校园类
            Anime anime8 = new Anime();
            anime8.setTitle("我的青春恋爱物语果然有问题");
            anime8.setDescription("高中生八幡的校园生活和人际关系故事。");
            anime8.setCoverImage("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=My%20Teen%20Romantic%20Comedy%20SNAFU%20anime%20cover&image_size=square_hd");
            anime8.setVideoUrl("https://www.youtube.com/embed/dQw4w9WgXcQ");
            anime8.setCategory(category5);
            animeService.saveAnime(anime8);

            // 恋爱类
            Anime anime9 = new Anime();
            anime9.setTitle("辉夜大小姐想让我告白");
            anime9.setDescription("两个高智商学生之间的恋爱头脑战。");
            anime9.setCoverImage("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Kaguya-sama%20Love%20is%20War%20anime%20cover&image_size=square_hd");
            anime9.setVideoUrl("https://www.youtube.com/embed/dQw4w9WgXcQ");
            anime9.setCategory(category6);
            animeService.saveAnime(anime9);

            // 喜剧类
            Anime anime10 = new Anime();
            anime10.setTitle("日常");
            anime10.setDescription("女高中生们的日常搞笑故事。");
            anime10.setCoverImage("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Nichijou%20anime%20cover%20image&image_size=square_hd");
            anime10.setVideoUrl("https://www.youtube.com/embed/dQw4w9WgXcQ");
            anime10.setCategory(category7);
            animeService.saveAnime(anime10);

            // 悬疑类
            Anime anime11 = new Anime();
            anime11.setTitle("死亡笔记");
            anime11.setDescription("高中生夜神月得到死亡笔记后的故事。");
            anime11.setCoverImage("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Death%20Note%20anime%20cover%20image&image_size=square_hd");
            anime11.setVideoUrl("https://www.youtube.com/embed/dQw4w9WgXcQ");
            anime11.setCategory(category8);
            animeService.saveAnime(anime11);

            // 恐怖类
            Anime anime12 = new Anime();
            anime12.setTitle("尸鬼");
            anime12.setDescription("一个村庄被尸鬼入侵的恐怖故事。");
            anime12.setCoverImage("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Shiki%20anime%20cover%20image&image_size=square_hd");
            anime12.setVideoUrl("https://www.youtube.com/embed/dQw4w9WgXcQ");
            anime12.setCategory(category9);
            animeService.saveAnime(anime12);

            // 魔法类
            Anime anime13 = new Anime();
            anime13.setTitle("魔法少女小圆");
            anime13.setDescription("普通少女成为魔法少女后的故事。");
            anime13.setCoverImage("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Madoka%20Magica%20anime%20cover&image_size=square_hd");
            anime13.setVideoUrl("https://www.youtube.com/embed/dQw4w9WgXcQ");
            anime13.setCategory(category10);
            animeService.saveAnime(anime13);

            // 机甲类
            Anime anime14 = new Anime();
            anime14.setTitle("新世纪福音战士");
            anime14.setDescription("少年碇真嗣驾驶EVA与使徒战斗的故事。");
            anime14.setCoverImage("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Neon%20Genesis%20Evangelion%20anime%20cover&image_size=square_hd");
            anime14.setVideoUrl("https://www.youtube.com/embed/dQw4w9WgXcQ");
            anime14.setCategory(category11);
            animeService.saveAnime(anime14);

            // 推理类
            Anime anime15 = new Anime();
            anime15.setTitle("名侦探柯南");
            anime15.setDescription("高中生侦探工藤新一变成小孩后解决各种案件的故事。");
            anime15.setCoverImage("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Detective%20Conan%20anime%20cover&image_size=square_hd");
            anime15.setVideoUrl("https://www.youtube.com/embed/dQw4w9WgXcQ");
            anime15.setCategory(category12);
            animeService.saveAnime(anime15);

            // 历史类
            Anime anime16 = new Anime();
            anime16.setTitle("进击的巨人 第二季");
            anime16.setDescription("人类与巨人的战斗进入新阶段。");
            anime16.setCoverImage("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Attack%20on%20Titan%20season%202%20anime%20cover&image_size=square_hd");
            anime16.setVideoUrl("https://www.youtube.com/embed/1g5dRc3sEFU");
            anime16.setCategory(category13);
            animeService.saveAnime(anime16);

            // 体育类
            Anime anime17 = new Anime();
            anime17.setTitle("排球少年");
            anime17.setDescription("少年日向翔阳的排球成长故事。");
            anime17.setCoverImage("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Haikyuu%20anime%20cover%20image&image_size=square_hd");
            anime17.setVideoUrl("https://www.youtube.com/embed/dQw4w9WgXcQ");
            anime17.setCategory(category14);
            animeService.saveAnime(anime17);

            // 音乐类
            Anime anime18 = new Anime();
            anime18.setTitle("轻音少女");
            anime18.setDescription("女子高中生乐队的音乐故事。");
            anime18.setCoverImage("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=K-On%20anime%20cover%20image&image_size=square_hd");
            anime18.setVideoUrl("https://www.youtube.com/embed/dQw4w9WgXcQ");
            anime18.setCategory(category15);
            animeService.saveAnime(anime18);
        }
    }
}

package com.zhelin.community.quartz;

import com.zhelin.community.entity.DiscussPost;
import com.zhelin.community.service.DiscussPostService;
import com.zhelin.community.service.ElasticSearchService;
import com.zhelin.community.service.LikeService;
import com.zhelin.community.util.CommunityConstant;
import com.zhelin.community.util.RedisKeyUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostScoreRefreshJob implements Job, CommunityConstant {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private ElasticSearchService elasticSearchService;

    private static final Logger logger = LoggerFactory.getLogger(PostScoreRefreshJob.class);
    private static final Date epoch;

    static {
        try {
            epoch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-01-01 00:00:00");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String redisKey = RedisKeyUtil.getPostScoreKey();
        BoundSetOperations operations = redisTemplate.boundSetOps(redisKey);

        if (operations.size() == 0) {
            logger.info("[Job cancelled] No post to refresh.");
            return;
        }
        logger.info("[Job started] Refreshing post scores" + operations.size());
        while(operations.size() > 0) {
            this.refresh((Integer) operations.pop());
        }
        logger.info("[Job done]");
    }

    private void refresh(int postId) {
        DiscussPost post = discussPostService.findDiscussPostById(postId);
        if (post == null) {
            logger.error("Can not find post by id: " + postId);
            return;
        }
        boolean wonderful = post.getStatus() == 1;
        int commentCount = post.getCommentCount();
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, postId);
        double weight = (wonderful ? 75 : 0) + commentCount * 10 + likeCount * 2;
        double score = Math.log10(Math.max(weight, 1)) + (post.getCreateTime().getTime() - epoch.getTime()) / (1000 * 3600 * 24);
        discussPostService.updateScore(postId, score);
        post.setScore(score);
        elasticSearchService.saveDiscussPost(post);
    }

}

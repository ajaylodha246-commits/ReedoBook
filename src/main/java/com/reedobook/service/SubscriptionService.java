package com.reedobook.service;

import com.reedobook.entity.*;
import com.reedobook.repository.Subscription_Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubscriptionService {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionService.class);

    private final Subscription_Repository subscriptionRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private CollegeService collegeService;

    @Autowired
    private UniversityService universityService;

    @Autowired
    private UserService userService;

    public SubscriptionService(Subscription_Repository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Transactional
    public Subscription saveSubscription(Subscription subscription) {
        logger.info("Saving subscription for userId: {}", subscription.getUserId());
        return subscriptionRepository.save(subscription);
    }

    public List<Subscription> findByUserId(Long userId) {
        logger.info("Fetching subscriptions for userId: {}", userId);
        List<Subscription> subscriptions = subscriptionRepository.findByUserId(userId);
        if (subscriptions.isEmpty()) {
            logger.info("No subscriptions found for userId: {}", userId);
        }
        return subscriptions;
    }

    public List<Subscription> findActiveSubscriptionByUserId(Long userId) {
        logger.info("Fetching active subscriptions for userId: {}", userId);
        List<Subscription> subscriptions = subscriptionRepository.findActiveSubscriptionByUserId(userId);
        if (subscriptions.isEmpty()) {
            logger.info("No subscriptions found for userId: {}", userId);
        } else{
            for(Subscription subscription : subscriptions){
                Course course = courseService.getCourseById(subscription.getCourseId()).get();
                subscription.setCourseName(course.getName());
                Semester semester = semesterService.getSemesterById(subscription.getSemesterId()).get();
                subscription.setSemesterName(semester.getName());
            }
        }
        return subscriptions;
    }

    public List<Subscription> findAllSubscriptions() {
        logger.info("Fetching all subscriptions");
        List<Subscription> subscriptionList = subscriptionRepository.findAll();
        for(Subscription subscription : subscriptionList){
            Course course = courseService.getCourseById(subscription.getCourseId()).get();
            subscription.setCourseName(course.getName());
            Semester semester = semesterService.getSemesterById(subscription.getSemesterId()).get();
            subscription.setSemesterName(semester.getName());
            University university = universityService.getUniversityById(subscription.getUniversityId()).get();
            subscription.setUniversityName(university.getName());
            College college = collegeService.getCollegeById(subscription.getCollegeId()).get();
            subscription.setCollegeName(college.getName());
            User user = userService.findUserByUserId(subscription.getUserId());
            subscription.setUserName(user.getName());
        }
        return subscriptionList;
    }
}

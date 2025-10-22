// ==========================================
// SHARED CONFIGURATION DATA
// Save as: src/config/homePageConfig.ts
// ==========================================
import { Clock, BookOpen, Users, Award } from 'lucide-react';

export interface BannerSlide {
  id: number;
  title: string;
  subtitle: string;
  image: string;
  buttonText: string;
  buttonLink: string;
  gradient: string;
}

export interface Feature {
  icon: any; // Lucide icon component
  title: string;
  description: string;
}

export interface Stat {
  value: string;
  label: string;
}

export interface TeacherData {
  tagline: string;
  teacherName: string;
  teacherRole: string;
  teacherImage: string;
  features: Feature[];
  stats: Stat[];
}

export interface ExamCategory {
  id: string;
  title: string;
  description: string;
  icon: string;
  color: string;
  link: string;
  subjects: string[];
}

export interface SuccessStory {
  id: number;
  studentName: string;
  achievement: string;
  image: string;
  score: string;
  testimonial: string;
}

// ==========================================
// PROFESSIONAL PROMOTIONAL BANNERS
// ==========================================

export const BANNER_SLIDES: BannerSlide[] = [
  {
    id: 1,
    title: '🪔 Diwali Special Offer!',
    subtitle: 'Light up your future with UPTO 80% OFF on all courses - Limited Time Only!',
    image: 'https://images.unsplash.com/photo-1513475382585-d06e58bcb0e0?w=1920&h=800&fit=crop&crop=center',
    buttonText: 'Grab Offer Now',
    buttonLink: '/diwali-offer',
    gradient: 'from-orange-500 to-red-600'
  },
  {
    id: 2,
    title: 'JEE 2026 Foundation',
    subtitle: 'Start Early, Succeed Big! Get 70% OFF on JEE Foundation Courses + Free Test Series',
    image: 'https://images.unsplash.com/photo-1522202176988-66273c2fd55f?w=1920&h=800&fit=crop&crop=center',
    buttonText: 'Enroll at ₹999',
    buttonLink: '/courses/jee-foundation',
    gradient: 'from-blue-600 to-indigo-700'
  },
  {
    id: 3,
    title: 'NEET 2025 Crash Course',
    subtitle: 'Last-minute preparation by Top Faculty - Only ₹1,999 (Was ₹4,999)',
    image: 'https://images.unsplash.com/photo-1576091160399-112ba8d25d1f?w=1920&h=800&fit=crop&crop=center',
    buttonText: 'Join Now - 60% OFF',
    buttonLink: '/courses/neet-crash',
    gradient: 'from-green-600 to-emerald-700'
  },
  {
    id: 4,
    title: 'Start Your Journey FREE!',
    subtitle: 'Experience quality education - Get 7-day free trial + Free doubt solving',
    image: 'https://images.unsplash.com/photo-1434030216411-0b793f4b4173?w=1920&h=800&fit=crop&crop=center',
    buttonText: 'Start Free Trial',
    buttonLink: '/free-trial',
    gradient: 'from-purple-600 to-pink-600'
  },
  {
    id: 5,
    title: '🏆 Scholarship Test',
    subtitle: 'Win up to 100% Scholarship! Register for Classtopper Scholarship Test 2025',
    image: 'https://images.unsplash.com/photo-1571260899304-425eee4c7efc?w=1920&h=800&fit=crop&crop=center',
    buttonText: 'Register Free',
    buttonLink: '/scholarship-test',
    gradient: 'from-yellow-600 to-orange-600'
  },
];

// ==========================================
// PROFESSIONAL TEACHER DATA
// ==========================================

export const TEACHER_DATA: TeacherData = {
  tagline: "Learn from India's Top Faculty",
  teacherName: 'Dr. Priya Sharma',
  teacherRole: 'Physics Expert & JEE Mentor',
  teacherImage: 'https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=400&h=500&fit=crop',
  features: [
    {
      icon: Clock,
      title: 'Daily Live Classes',
      description: 'Interactive sessions with real-time doubt solving and personalized attention'
    },
    {
      icon: BookOpen,
      title: 'Complete Study Material',
      description: 'Comprehensive notes, test papers and previous year questions'
    },
    {
      icon: Users,
      title: '24/7 Doubt Support',
      description: 'Instant doubt resolution through chat, video calls and discussion forums'
    },
    {
      icon: Award,
      title: 'Proven Results',
      description: '500+ students in top colleges with 95% success rate'
    }
  ],
  stats: [
    { value: '15+', label: 'Years Experience' },
    { value: '10,000+', label: 'Students Taught' },
    { value: '4.9/5', label: 'Rating' },
    { value: '95%', label: 'Success Rate' }
  ]
};

// ==========================================
// PROFESSIONAL EXAM CATEGORIES
// ==========================================

export const EXAM_CATEGORIES: ExamCategory[] = [
  {
    id: 'jee-main',
    title: 'JEE Main & Advanced',
    description: 'Complete preparation for engineering entrance',
    icon: '🚀',
    color: 'from-blue-500 to-blue-600',
    link: '/courses?category=jee',
    subjects: ['Physics', 'Chemistry', 'Mathematics']
  },
  {
    id: 'neet',
    title: 'NEET UG',
    description: 'Medical entrance exam preparation',
    icon: '🏥',
    color: 'from-green-500 to-green-600',
    link: '/courses?category=neet',
    subjects: ['Physics', 'Chemistry', 'Biology']
  },
  {
    id: 'class-12',
    title: 'Class 12 Boards',
    description: 'CBSE, ISC & State Board preparation',
    icon: '📚',
    color: 'from-purple-500 to-purple-600',
    link: '/courses?category=class-12',
    subjects: ['PCM', 'PCB', 'Commerce']
  },
  {
    id: 'class-10',
    title: 'Class 10 Boards',
    description: 'Strong foundation for board exams',
    icon: '🎯',
    color: 'from-indigo-500 to-indigo-600',
    link: '/courses?category=class-10',
    subjects: ['Math', 'Science', 'Social Studies']
  },
  {
    id: 'foundation',
    title: 'Foundation (6-9)',
    description: 'Strong conceptual foundation',
    icon: '⭐',
    color: 'from-orange-500 to-orange-600',
    link: '/courses?category=foundation',
    subjects: ['Math', 'Science', 'English']
  },
  {
    id: 'test-series',
    title: 'Test Series',
    description: 'Mock tests & practice papers',
    icon: '📝',
    color: 'from-red-500 to-red-600',
    link: '/test-series',
    subjects: ['All Subjects']
  }
];

// ==========================================
// SUCCESS STORIES WITH BETTER DATA
// ==========================================

export const SUCCESS_STORIES: SuccessStory[] = [
  {
    id: 1,
    studentName: 'Rahul Kumar',
    achievement: 'AIR 47 - JEE Advanced 2024',
    image: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&h=200&fit=crop',
    score: '99.8%',
    testimonial: 'Classtopper\'s faculty and study material helped me crack JEE with confidence. The daily practice tests were game-changers!'
  },
  {
    id: 2,
    studentName: 'Priya Singh',
    achievement: '98.2% - CBSE Class 12',
    image: 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200&h=200&fit=crop',
    score: '98.2%',
    testimonial: 'Excellent teaching methodology and personalized attention. Best investment in my academic career.'
  },
  {
    id: 3,
    studentName: 'Arjun Patel',
    achievement: 'AIR 123 - NEET 2024',
    image: 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200&h=200&fit=crop',
    score: '99.5%',
    testimonial: 'Best platform for medical entrance preparation. The doubt-solving sessions were incredibly helpful.'
  },
  {
    id: 4,
    studentName: 'Ananya Sharma',
    achievement: '96.8% - Class 10 CBSE',
    image: 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=200&h=200&fit=crop',
    score: '96.8%',
    testimonial: 'Thanks to Classtopper, I scored excellent marks and built a strong foundation for Class 11.'
  },
  {
    id: 5,
    studentName: 'Vikash Gupta',
    achievement: 'AIR 89 - JEE Main 2024',
    image: 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=200&h=200&fit=crop',
    score: '99.2%',
    testimonial: 'The structured approach and regular assessments helped me stay on track throughout my preparation.'
  }
];

// ==========================================
// ADDITIONAL PROMOTIONAL DATA
// ==========================================

export const SPECIAL_OFFERS = [
  {
    id: 'diwali-2025',
    title: 'Diwali Festival Sale',
    description: 'Up to 80% OFF on all courses',
    discount: '80%',
    validUntil: '2025-11-15',
    courses: ['JEE', 'NEET', 'Boards'],
    featured: true
  },
  {
    id: 'scholarship-test',
    title: 'Scholarship Test 2025',
    description: 'Win up to 100% scholarship',
    discount: '100%',
    validUntil: '2025-12-31',
    courses: ['All Courses'],
    featured: true
  },
  {
    id: 'free-trial',
    title: '7-Day Free Trial',
    description: 'Experience premium education for free',
    discount: 'FREE',
    validUntil: 'Ongoing',
    courses: ['All Courses'],
    featured: false
  }
];

export const TRUST_INDICATORS = {
  totalStudents: '50,000+',
  successRate: '95%',
  topRankers: '1,200+',
  yearsOfExperience: '10+',
  averageRating: '4.9/5',
  coursesOffered: '50+'
};

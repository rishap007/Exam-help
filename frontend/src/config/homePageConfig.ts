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

// Configuration Data


export const BANNER_SLIDES: BannerSlide[] = [
  {
    id: 1,
    title: 'Start Your Learning Journey',
    subtitle: 'Join thousands of students achieving their dreams',
    image: 'https://images.unsplash.com/photo-1523050854058-8df90110c9f1?w=1200&h=500&fit=crop',
    buttonText: 'Explore Courses',
    buttonLink: '/courses',
    gradient: 'from-blue-600/90 to-purple-600/90'
  },
  {
    id: 2,
    title: 'Board Exam Excellence',
    subtitle: 'Expert faculty for Classes 6-12',
    image: 'https://images.unsplash.com/photo-1427504494785-3a9ca7044f45?w=1200&h=500&fit=crop',
    buttonText: 'View Programs',
    buttonLink: '/courses?category=boards',
    gradient: 'from-green-600/90 to-teal-600/90'
  },
  {
    id: 3,
    title: 'JEE Foundation Courses',
    subtitle: 'Build a strong foundation for engineering entrance',
    image: 'https://images.unsplash.com/photo-1509062522246-3755977927d7?w=1200&h=500&fit=crop',
    buttonText: 'Get Started',
    buttonLink: '/courses?category=jee',
    gradient: 'from-orange-600/90 to-red-600/90'
  },
];

export const TEACHER_DATA: TeacherData = {
  tagline: "Learn from India's Top Faculty",
  teacherName: 'Dr. Priya Sharma',
  teacherRole: 'Physics Expert & JEE Mentor',
  teacherImage: 'https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=400&h=500&fit=crop',
  features: [
    {
      icon: Clock,
      title: 'Daily Live Classes',
      description: 'Interactive sessions with real-time doubt solving'
    },
    {
      icon: BookOpen,
      title: 'Test Papers & Notes',
      description: 'Comprehensive study material and practice tests'
    },
    {
      icon: Users,
      title: 'Doubt Solving Support',
      description: '24/7 mentor support for all your queries'
    },
    {
      icon: Award,
      title: 'Proven Track Record',
      description: '500+ students in top colleges'
    }
  ],
  stats: [
    { value: '15+', label: 'Years Experience' },
    { value: '10,000+', label: 'Students Taught' },
    { value: '4.9/5', label: 'Rating' }
  ]
};

export const EXAM_CATEGORIES: ExamCategory[] = [
  {
    id: 'class-10',
    title: '10th Boards',
    description: 'CBSE, ICSE & State Boards',
    icon: '📚',
    color: 'from-blue-500 to-cyan-500',
    link: '/courses?category=class-10',
    subjects: ['Math', 'Science', 'Social Studies']
  },
  {
    id: 'class-12',
    title: '12th Boards',
    description: 'PCM, PCB & Commerce',
    icon: '🎓',
    color: 'from-purple-500 to-pink-500',
    link: '/courses?category=class-12',
    subjects: ['Physics', 'Chemistry', 'Math']
  },
  {
    id: 'school-prep',
    title: 'School Prep',
    description: 'Classes 6-9 Foundation',
    icon: '✏️',
    color: 'from-green-500 to-teal-500',
    link: '/courses?category=school',
    subjects: ['All Subjects']
  },
  {
    id: 'jee-foundation',
    title: 'JEE Foundation',
    description: 'Classes 9-10 JEE Prep',
    icon: '🚀',
    color: 'from-orange-500 to-red-500',
    link: '/courses?category=jee-foundation',
    subjects: ['Math', 'Physics', 'Chemistry']
  }
];

export const SUCCESS_STORIES: SuccessStory[] = [
  {
    id: 1,
    studentName: 'Rahul Kumar',
    achievement: 'AIR 47 - JEE Advanced',
    image: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&h=200&fit=crop',
    score: '99.8%',
    testimonial: 'The faculty and study material helped me crack JEE with confidence.'
  },
  {
    id: 2,
    studentName: 'Priya Singh',
    achievement: '98.2% - CBSE Class 12',
    image: 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200&h=200&fit=crop',
    score: '98.2%',
    testimonial: 'Excellent teaching methodology and personalized attention.'
  },
  {
    id: 3,
    studentName: 'Arjun Patel',
    achievement: 'AIR 123 - NEET',
    image: 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200&h=200&fit=crop',
    score: '99.5%',
    testimonial: 'Best platform for medical entrance preparation.'
  }
];

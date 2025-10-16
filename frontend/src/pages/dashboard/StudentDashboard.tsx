import { Link } from 'react-router-dom';
import { BookOpen, Clock, Trophy, Play, CheckCircle2, ArrowRight } from 'lucide-react';
import { useAuthStore } from '@/stores/authStore';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Badge } from '@/components/ui/Badge';
// import { Avatar } from '@/components/ui/Avatar';
// import { Skeleton } from '@/components/ui/Skeleton';

export const StudentDashboard = () => {
  const user = useAuthStore((state) => state.user);

  // Mock data - will be replaced with real API calls
  const stats = {
    enrolledCourses: 5,
    completedCourses: 2,
    learningHours: 48,
    certificates: 2,
  };

  const recentCourses = [
    {
      id: '1',
      title: 'React.js Complete Guide',
      instructor: 'John Doe',
      progress: 65,
      thumbnailUrl: '',
      lastAccessed: '2 hours ago',
    },
    {
      id: '2',
      title: 'TypeScript Fundamentals',
      instructor: 'Jane Smith',
      progress: 30,
      thumbnailUrl: '',
      lastAccessed: '1 day ago',
    },
    {
      id: '3',
      title: 'Node.js Backend Development',
      instructor: 'Mike Johnson',
      progress: 15,
      thumbnailUrl: '',
      lastAccessed: '3 days ago',
    },
  ];

  const upcomingDeadlines = [
    {
      id: '1',
      title: 'React Hooks Assignment',
      course: 'React.js Complete Guide',
      dueDate: '2024-01-15',
      type: 'assignment',
    },
    {
      id: '2',
      title: 'TypeScript Quiz',
      course: 'TypeScript Fundamentals',
      dueDate: '2024-01-18',
      type: 'quiz',
    },
  ];

  return (
    <div className="space-y-8">
      {/* Welcome Section */}
      <div>
        <h1 className="text-3xl font-bold">
          Welcome back, {user?.firstName}! 👋
        </h1>
        <p className="text-muted-foreground mt-2">
          Continue your learning journey and achieve your goals
        </p>
      </div>

      {/* Stats Grid */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">
              Enrolled Courses
            </CardTitle>
            <BookOpen className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.enrolledCourses}</div>
            <p className="text-xs text-muted-foreground">
              Active learning paths
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">
              Completed
            </CardTitle>
            <CheckCircle2 className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.completedCourses}</div>
            <p className="text-xs text-muted-foreground">
              Courses finished
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">
              Learning Hours
            </CardTitle>
            <Clock className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.learningHours}h</div>
            <p className="text-xs text-muted-foreground">
              Total time invested
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">
              Certificates
            </CardTitle>
            <Trophy className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.certificates}</div>
            <p className="text-xs text-muted-foreground">
              Achievements earned
            </p>
          </CardContent>
        </Card>
      </div>

      <div className="grid gap-6 lg:grid-cols-3">
        {/* Continue Learning */}
        <div className="lg:col-span-2">
          <Card>
            <CardHeader>
              <CardTitle>Continue Learning</CardTitle>
              <CardDescription>
                Pick up where you left off
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              {recentCourses.map((course) => (
                <div
                  key={course.id}
                  className="flex items-center gap-4 rounded-lg border p-4 transition-colors hover:bg-accent"
                >
                  <div className="flex h-16 w-16 items-center justify-center rounded-md bg-primary/10">
                    <Play className="h-6 w-6 text-primary" />
                  </div>
                  <div className="flex-1 space-y-1">
                    <h3 className="font-semibold leading-none">{course.title}</h3>
                    <p className="text-sm text-muted-foreground">
                      by {course.instructor}
                    </p>
                    <div className="flex items-center gap-2">
                      <div className="h-2 flex-1 rounded-full bg-muted">
                        <div
                          className="h-full rounded-full bg-primary"
                          style={{ width: `${course.progress}%` }}
                        />
                      </div>
                      <span className="text-xs text-muted-foreground">
                        {course.progress}%
                      </span>
                    </div>
                  </div>
                  <Button size="sm" asChild>
                    <Link to={`/courses/${course.id}`}>
                      Continue
                      <ArrowRight className="ml-2 h-4 w-4" />
                    </Link>
                  </Button>
                </div>
              ))}

              <Button variant="outline" className="w-full" asChild>
                <Link to="/my-courses">View All Courses</Link>
              </Button>
            </CardContent>
          </Card>
        </div>

        {/* Sidebar */}
        <div className="space-y-6">
          {/* Upcoming Deadlines */}
          <Card>
            <CardHeader>
              <CardTitle>Upcoming Deadlines</CardTitle>
              <CardDescription>
                Don't miss these assignments
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              {upcomingDeadlines.map((item) => (
                <div key={item.id} className="space-y-2">
                  <div className="flex items-start justify-between">
                    <div className="space-y-1">
                      <p className="font-medium leading-none">{item.title}</p>
                      <p className="text-sm text-muted-foreground">
                        {item.course}
                      </p>
                    </div>
                    <Badge variant={item.type === 'assignment' ? 'default' : 'secondary'}>
                      {item.type}
                    </Badge>
                  </div>
                  <p className="text-xs text-muted-foreground">
                    Due: {new Date(item.dueDate).toLocaleDateString()}
                  </p>
                </div>
              ))}

              {upcomingDeadlines.length === 0 && (
                <p className="text-sm text-muted-foreground text-center py-4">
                  No upcoming deadlines
                </p>
              )}
            </CardContent>
          </Card>

          {/* Learning Streak */}
          <Card>
            <CardHeader>
              <CardTitle>Learning Streak</CardTitle>
              <CardDescription>
                Keep up the momentum!
              </CardDescription>
            </CardHeader>
            <CardContent>
              <div className="flex items-center justify-center">
                <div className="text-center">
                  <div className="text-4xl font-bold text-primary">7</div>
                  <p className="text-sm text-muted-foreground mt-1">
                    Days in a row
                  </p>
                </div>
              </div>
              <div className="mt-4 flex justify-center gap-1">
                {[...Array(7)].map((_, i) => (
                  <div
                    key={i}
                    className="h-8 w-8 rounded-md bg-primary/20 flex items-center justify-center"
                  >
                    <CheckCircle2 className="h-4 w-4 text-primary" />
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>

          {/* Recommended Course */}
          <Card>
            <CardHeader>
              <CardTitle>Recommended for You</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                <div className="aspect-video rounded-md bg-muted" />
                <div>
                  <h3 className="font-semibold">Advanced React Patterns</h3>
                  <p className="text-sm text-muted-foreground mt-1">
                    Master advanced React techniques
                  </p>
                </div>
                <Button size="sm" className="w-full" asChild>
                  <Link to="/courses/recommended">
                    Explore Course
                  </Link>
                </Button>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
};
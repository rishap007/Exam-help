import { useState } from 'react';
import { Link } from 'react-router-dom';
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Badge } from '@/components/ui/Badge';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/Tab';
import { EmptyState } from '@/components/common/EmptyState';
import { BookOpen, Clock, PlayCircle, CheckCircle2, Award, TrendingUp } from 'lucide-react';
import { formatDate } from '@/utils/format';

export const MyCoursesPage = () => {
  const [selectedTab, setSelectedTab] = useState('in-progress');

  // Mock data - will be replaced with real API calls
  const inProgressCourses = [
    {
      id: '1',
      title: 'React.js Complete Guide',
      instructor: 'John Doe',
      progress: 65,
      thumbnailUrl: '',
      lastAccessed: '2024-01-15T10:30:00Z',
      totalLessons: 45,
      completedLessons: 29,
      estimatedTime: '12h 30m remaining',
    },
    {
      id: '2',
      title: 'TypeScript Fundamentals',
      instructor: 'Jane Smith',
      progress: 30,
      thumbnailUrl: '',
      lastAccessed: '2024-01-14T15:20:00Z',
      totalLessons: 30,
      completedLessons: 9,
      estimatedTime: '18h remaining',
    },
  ];

  const completedCourses = [
    {
      id: '3',
      title: 'JavaScript Basics',
      instructor: 'Bob Johnson',
      completedDate: '2024-01-01T00:00:00Z',
      certificateUrl: '/certificates/js-basics',
      rating: 5,
    },
  ];

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-bold">My Courses</h1>
        <p className="text-muted-foreground mt-2">
          Track your learning progress and continue where you left off
        </p>
      </div>

      {/* Stats Overview */}
      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">
              Enrolled Courses
            </CardTitle>
            <BookOpen className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">5</div>
            <p className="text-xs text-muted-foreground">
              Active learning paths
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">
              In Progress
            </CardTitle>
            <TrendingUp className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">3</div>
            <p className="text-xs text-muted-foreground">
              Currently learning
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
            <div className="text-2xl font-bold">2</div>
            <p className="text-xs text-muted-foreground">
              Courses finished
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">
              Certificates
            </CardTitle>
            <Award className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">2</div>
            <p className="text-xs text-muted-foreground">
              Earned achievements
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Course Tabs */}
      <Tabs value={selectedTab} onValueChange={setSelectedTab}>
        <TabsList>
          <TabsTrigger value="in-progress">In Progress</TabsTrigger>
          <TabsTrigger value="completed">Completed</TabsTrigger>
          <TabsTrigger value="saved">Saved</TabsTrigger>
        </TabsList>

        <TabsContent value="in-progress" className="space-y-4">
          {inProgressCourses.length === 0 ? (
            <EmptyState
              icon={BookOpen}
              title="No courses in progress"
              description="Start learning by enrolling in a course"
              action={{
                label: 'Browse Courses',
                onClick: () => window.location.href = '/courses',
              }}
            />
          ) : (
            <div className="grid gap-6 md:grid-cols-2">
              {inProgressCourses.map((course) => (
                <Card key={course.id} className="flex flex-col">
                  <div className="relative">
                    {course.thumbnailUrl ? (
                      <img
                        src={course.thumbnailUrl}
                        alt={course.title}
                        className="h-48 w-full rounded-t-lg object-cover"
                      />
                    ) : (
                      <div className="flex h-48 w-full items-center justify-center rounded-t-lg bg-muted">
                        <BookOpen className="h-12 w-12 text-muted-foreground" />
                      </div>
                    )}
                    <Badge variant="secondary" className="absolute left-2 top-2">
                      {course.progress}% Complete
                    </Badge>
                  </div>

                  <CardHeader>
                    <CardTitle className="line-clamp-2">{course.title}</CardTitle>
                    <CardDescription>by {course.instructor}</CardDescription>
                  </CardHeader>

                  <CardContent className="flex-1 space-y-4">
                    <div>
                      <div className="mb-2 flex justify-between text-sm">
                        <span className="text-muted-foreground">Progress</span>
                        <span className="font-medium">{course.completedLessons}/{course.totalLessons} lessons</span>
                      </div>
                      <div className="h-2 w-full rounded-full bg-muted">
                        <div
                          className="h-full rounded-full bg-primary transition-all"
                          style={{ width: `${course.progress}%` }}
                        />
                      </div>
                    </div>

                    <div className="flex items-center gap-4 text-sm text-muted-foreground">
                      <div className="flex items-center gap-1">
                        <Clock className="h-4 w-4" />
                        <span>{course.estimatedTime}</span>
                      </div>
                    </div>

                    <p className="text-xs text-muted-foreground">
                      Last accessed {formatDate(course.lastAccessed)}
                    </p>
                  </CardContent>

                  <CardFooter>
                    <Button className="w-full" asChild>
                      <Link to={`/learn/${course.id}`}>
                        <PlayCircle className="mr-2 h-4 w-4" />
                        Continue Learning
                      </Link>
                    </Button>
                  </CardFooter>
                </Card>
              ))}
            </div>
          )}
        </TabsContent>

        <TabsContent value="completed" className="space-y-4">
          {completedCourses.length === 0 ? (
            <EmptyState
              icon={CheckCircle2}
              title="No completed courses yet"
              description="Complete a course to earn your certificate"
            />
          ) : (
            <div className="grid gap-6 md:grid-cols-2">
              {completedCourses.map((course) => (
                <Card key={course.id}>
                  <CardHeader>
                    <div className="flex items-start justify-between">
                      <div className="flex-1">
                        <CardTitle>{course.title}</CardTitle>
                        <CardDescription>by {course.instructor}</CardDescription>
                      </div>
                      <Badge variant="success">
                        <CheckCircle2 className="mr-1 h-3 w-3" />
                        Completed
                      </Badge>
                    </div>
                  </CardHeader>

                  <CardContent className="space-y-4">
                    <div className="flex items-center gap-1">
                      {[...Array(5)].map((_, i) => (
                        <span
                          key={i}
                          className={`text-lg ${
                            i < course.rating ? 'text-yellow-400' : 'text-muted'
                          }`}
                        >
                          ★
                        </span>
                      ))}
                    </div>

                    <p className="text-sm text-muted-foreground">
                      Completed on {formatDate(course.completedDate)}
                    </p>
                  </CardContent>

                  <CardFooter className="flex gap-2">
                    <Button variant="outline" className="flex-1" asChild>
                      <Link to={`/courses/${course.id}`}>View Course</Link>
                    </Button>
                    {course.certificateUrl && (
                      <Button className="flex-1" asChild>
                        <a href={course.certificateUrl} target="_blank" rel="noopener noreferrer">
                          <Award className="mr-2 h-4 w-4" />
                          Certificate
                        </a>
                      </Button>
                    )}
                  </CardFooter>
                </Card>
              ))}
            </div>
          )}
        </TabsContent>

        <TabsContent value="saved">
          <EmptyState
            icon={BookOpen}
            title="No saved courses"
            description="Save courses you're interested in for later"
            action={{
              label: 'Browse Courses',
              onClick: () => window.location.href = '/courses',
            }}
          />
        </TabsContent>
      </Tabs>
    </div>
  );
};
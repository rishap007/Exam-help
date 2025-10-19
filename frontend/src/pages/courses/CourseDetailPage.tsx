import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useCourse } from '@/hooks/useCourse';
import { useAuthStore } from '@/stores/authStore';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Badge } from '@/components/ui/Badge';
import { Avatar } from '@/components/ui/Avatar';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/Tab';
import { Alert, AlertDescription } from '@/components/ui/Alert';
import { Skeleton } from '@/components/ui/Skeleton';
import {
  BookOpen,
  Clock,
  Star,
  Users,
  CheckCircle2,
  Globe,
  ChevronRight,
} from 'lucide-react';
import { formatCurrency } from '@/utils/format';

export const CourseDetailPage = () => {
  const { slug } = useParams<{ slug: string }>();
  const navigate = useNavigate();
//   const user = useAuthStore((state) => state.user);
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);

  const { data: course, isLoading, isError } = useCourse(slug!);
  const [selectedTab, setSelectedTab] = useState('overview');

  const handleEnroll = () => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    // TODO: Implement enrollment logic
    console.log('Enrolling in course:', course?.id);
  };

  if (isLoading) {
    return (
      <div className="space-y-6">
        <Skeleton className="h-64 w-full" />
        <div className="grid gap-6 lg:grid-cols-3">
          <div className="lg:col-span-2 space-y-4">
            <Skeleton className="h-32 w-full" />
            <Skeleton className="h-48 w-full" />
          </div>
          <Skeleton className="h-96 w-full" />
        </div>
      </div>
    );
  }

  if (isError || !course) {
    return (
      <Alert variant="destructive">
        <AlertDescription>
          Course not found or failed to load. Please try again.
        </AlertDescription>
      </Alert>
    );
  }

  const isEnrolled = false; // TODO: Check if user is enrolled
  const isFree = !course.price || course.price === 0;

  return (
    <div className="space-y-8">
      {/* Hero Section */}
      <div className="relative overflow-hidden rounded-lg bg-gradient-to-r from-primary/10 via-primary/5 to-background p-8 md:p-12">
        <div className="relative z-10 grid gap-8 lg:grid-cols-2">
          {/* Left Column - Course Info */}
          <div className="space-y-6">
            <div className="space-y-2">
              <Badge variant="secondary">{course.level}</Badge>
              <h1 className="text-4xl font-bold">{course.title}</h1>
              <p className="text-lg text-muted-foreground">
                {course.shortDescription}
              </p>
            </div>

            {/* Course Stats */}
            <div className="flex flex-wrap gap-6 text-sm">
              <div className="flex items-center gap-2">
                <Star className="h-5 w-5 fill-yellow-400 text-yellow-400" />
                <span className="font-semibold">
                  {course.averageRating?.toFixed(1) || 'New'}
                </span>
              </div>
              <div className="flex items-center gap-2">
                <Users className="h-5 w-5" />
                <span>{course.enrollmentCount} students</span>
              </div>
              {course.durationHours && (
                <div className="flex items-center gap-2">
                  <Clock className="h-5 w-5" />
                  <span>{course.durationHours} hours</span>
                </div>
              )}
              <div className="flex items-center gap-2">
                <Globe className="h-5 w-5" />
                <span>{course.language}</span>
              </div>
            </div>

            {/* Instructor */}
            <div className="flex items-center gap-3">
              <Avatar
                src={course.instructor.profilePictureUrl}
                fallback={`${course.instructor.firstName} ${course.instructor.lastName}`}
                size="md"
              />
              <div>
                <p className="text-sm text-muted-foreground">Instructor</p>
                <p className="font-semibold">
                  {course.instructor.firstName} {course.instructor.lastName}
                </p>
              </div>
            </div>

            {/* Action Buttons - Mobile */}
            <div className="lg:hidden space-y-2">
              {isEnrolled ? (
                <Button size="lg" className="w-full" asChild>
                  <a href={`/learn/${course.slug}`}>
                    Continue Learning
                    <ChevronRight className="ml-2 h-4 w-4" />
                  </a>
                </Button>
              ) : (
                <Button size="lg" className="w-full" onClick={handleEnroll}>
                  {isFree ? 'Enroll for Free' : `Enroll for ${formatCurrency(course.discountPrice || course.price!, course.currency)}`}
                </Button>
              )}
            </div>
          </div>

          {/* Right Column - Course Preview */}
          <div className="hidden lg:block">
            <Card>
              <CardContent className="p-0">
                {course.trailerUrl ? (
                  <div className="relative aspect-video bg-muted">
                    <video
                      src={course.trailerUrl}
                      controls
                      className="h-full w-full rounded-t-lg"
                    />
                  </div>
                ) : course.thumbnailUrl ? (
                  <img
                    src={course.thumbnailUrl}
                    alt={course.title}
                    className="h-full w-full rounded-t-lg object-cover aspect-video"
                  />
                ) : (
                  <div className="flex aspect-video items-center justify-center bg-muted rounded-t-lg">
                    <BookOpen className="h-16 w-16 text-muted-foreground" />
                  </div>
                )}
                <div className="p-6 space-y-4">
                  <div>
                    {isFree ? (
                      <div className="text-3xl font-bold">Free</div>
                    ) : (
                      <div className="flex items-baseline gap-2">
                        <span className="text-3xl font-bold">
                          {formatCurrency(course.discountPrice || course.price!, course.currency)}
                        </span>
                        {course.discountPrice && (
                          <span className="text-lg text-muted-foreground line-through">
                            {formatCurrency(course.price!, course.currency)}
                          </span>
                        )}
                      </div>
                    )}
                  </div>

                  {isEnrolled ? (
                    <Button size="lg" className="w-full" asChild>
                      <a href={`/learn/${course.slug}`}>
                        Continue Learning
                        <ChevronRight className="ml-2 h-4 w-4" />
                      </a>
                    </Button>
                  ) : (
                    <Button size="lg" className="w-full" onClick={handleEnroll}>
                      {isFree ? 'Enroll for Free' : 'Enroll Now'}
                    </Button>
                  )}

                  <div className="space-y-2 text-sm">
                    <div className="flex items-center justify-between">
                      <span className="text-muted-foreground">Duration</span>
                      <span className="font-medium">{course.durationHours}h total</span>
                    </div>
                    <div className="flex items-center justify-between">
                      <span className="text-muted-foreground">Level</span>
                      <span className="font-medium">{course.level}</span>
                    </div>
                    <div className="flex items-center justify-between">
                      <span className="text-muted-foreground">Enrolled</span>
                      <span className="font-medium">{course.enrollmentCount} students</span>
                    </div>
                    <div className="flex items-center justify-between">
                      <span className="text-muted-foreground">Language</span>
                      <span className="font-medium">{course.language}</span>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>
        </div>
      </div>

      {/* Course Content */}
      <Tabs value={selectedTab} onValueChange={setSelectedTab}>
        <TabsList className="grid w-full grid-cols-4">
          <TabsTrigger value="overview">Overview</TabsTrigger>
          <TabsTrigger value="curriculum">Curriculum</TabsTrigger>
          <TabsTrigger value="instructor">Instructor</TabsTrigger>
          <TabsTrigger value="reviews">Reviews</TabsTrigger>
        </TabsList>

        <TabsContent value="overview" className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle>About this course</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <p className="text-muted-foreground whitespace-pre-wrap">
                {course.description}
              </p>
            </CardContent>
          </Card>

          {course.learningObjectives && (
            <Card>
              <CardHeader>
                <CardTitle>What you'll learn</CardTitle>
              </CardHeader>
              <CardContent>
                <ul className="grid gap-3 md:grid-cols-2">
                  {course.learningObjectives.split('\n').map((objective, index) => (
                    <li key={index} className="flex items-start gap-2">
                      <CheckCircle2 className="h-5 w-5 shrink-0 text-primary mt-0.5" />
                      <span>{objective}</span>
                    </li>
                  ))}
                </ul>
              </CardContent>
            </Card>
          )}

          {course.prerequisites && (
            <Card>
              <CardHeader>
                <CardTitle>Prerequisites</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-muted-foreground">{course.prerequisites}</p>
              </CardContent>
            </Card>
          )}

          {course.targetAudience && (
            <Card>
              <CardHeader>
                <CardTitle>Who this course is for</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-muted-foreground">{course.targetAudience}</p>
              </CardContent>
            </Card>
          )}
        </TabsContent>

        <TabsContent value="curriculum">
          <Card>
            <CardHeader>
              <CardTitle>Course Curriculum</CardTitle>
              <CardDescription>
                Lessons and assignments for this course
              </CardDescription>
            </CardHeader>
            <CardContent>
              <p className="text-muted-foreground text-center py-8">
                Curriculum content coming soon...
              </p>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="instructor">
          <Card>
            <CardHeader>
              <div className="flex items-center gap-4">
                <Avatar
                  src={course.instructor.profilePictureUrl}
                  fallback={`${course.instructor.firstName} ${course.instructor.lastName}`}
                  size="lg"
                />
                <div>
                  <CardTitle>
                    {course.instructor.firstName} {course.instructor.lastName}
                  </CardTitle>
                  <CardDescription>{course.instructor.email}</CardDescription>
                </div>
              </div>
            </CardHeader>
            <CardContent>
              {course.instructor.bio ? (
                <p className="text-muted-foreground">{course.instructor.bio}</p>
              ) : (
                <p className="text-muted-foreground">No bio available</p>
              )}
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="reviews">
          <Card>
            <CardHeader>
              <CardTitle>Student Reviews</CardTitle>
              <CardDescription>
                What students are saying about this course
              </CardDescription>
            </CardHeader>
            <CardContent>
              <p className="text-muted-foreground text-center py-8">
                Reviews coming soon...
              </p>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  );
};
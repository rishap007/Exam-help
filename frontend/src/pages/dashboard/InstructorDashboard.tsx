import { Link } from 'react-router-dom';
import { BookOpen, Users, DollarSign, TrendingUp, Plus, Eye, Edit, BarChart3 } from 'lucide-react';
// import { useAuthStore } from '@/stores/authStore';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Badge } from '@/components/ui/Badge';
import { DataTable } from '@/components/common/DataTable';

export const InstructorDashboard = () => {
//   const user = useAuthStore((state) => state.user);

  // Mock data
  const stats = {
    totalCourses: 8,
    totalStudents: 1247,
    totalRevenue: 45890,
    avgRating: 4.7,
  };

  const recentCourses = [
    {
      id: '1',
      title: 'React.js Complete Guide',
      students: 324,
      revenue: 12960,
      rating: 4.8,
      status: 'published',
    },
    {
      id: '2',
      title: 'TypeScript Fundamentals',
      students: 198,
      revenue: 7920,
      rating: 4.6,
      status: 'published',
    },
    {
      id: '3',
      title: 'Advanced Node.js',
      students: 0,
      revenue: 0,
      rating: 0,
      status: 'draft',
    },
  ];

  const recentStudents = [
    {
      id: '1',
      name: 'Alice Johnson',
      email: 'alice@example.com',
      course: 'React.js Complete Guide',
      enrolledAt: '2024-01-10',
      progress: 45,
    },
    {
      id: '2',
      name: 'Bob Smith',
      email: 'bob@example.com',
      course: 'TypeScript Fundamentals',
      enrolledAt: '2024-01-09',
      progress: 78,
    },
    {
      id: '3',
      name: 'Carol Davis',
      email: 'carol@example.com',
      course: 'React.js Complete Guide',
      enrolledAt: '2024-01-08',
      progress: 92,
    },
  ];

  const pendingReviews = [
    {
      id: '1',
      student: 'Alice Johnson',
      assignment: 'React Hooks Project',
      course: 'React.js Complete Guide',
      submittedAt: '2 hours ago',
    },
    {
      id: '2',
      student: 'Bob Smith',
      assignment: 'TypeScript Quiz',
      course: 'TypeScript Fundamentals',
      submittedAt: '1 day ago',
    },
  ];

  return (
    <div className="space-y-8">
      {/* Welcome Section */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold">
            Instructor Dashboard
          </h1>
          <p className="text-muted-foreground mt-2">
            Manage your courses and track student progress
          </p>
        </div>
        <Button asChild>
          <Link to="/instructor/courses/new">
            <Plus className="mr-2 h-4 w-4" />
            Create Course
          </Link>
        </Button>
      </div>

      {/* Stats Grid */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">
              Total Courses
            </CardTitle>
            <BookOpen className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.totalCourses}</div>
            <p className="text-xs text-muted-foreground">
              3 published, 2 drafts
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">
              Total Students
            </CardTitle>
            <Users className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.totalStudents}</div>
            <p className="text-xs text-muted-foreground">
              +12% from last month
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">
              Total Revenue
            </CardTitle>
            <DollarSign className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">${stats.totalRevenue.toLocaleString()}</div>
            <p className="text-xs text-muted-foreground">
              +8% from last month
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">
              Avg. Rating
            </CardTitle>
            <TrendingUp className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.avgRating}</div>
            <p className="text-xs text-muted-foreground">
              Across all courses
            </p>
          </CardContent>
        </Card>
      </div>

      <div className="grid gap-6 lg:grid-cols-3">
        {/* Your Courses */}
        <div className="lg:col-span-2">
          <Card>
            <CardHeader>
              <div className="flex items-center justify-between">
                <div>
                  <CardTitle>Your Courses</CardTitle>
                  <CardDescription>
                    Manage and track your courses
                  </CardDescription>
                </div>
                <Button variant="outline" size="sm" asChild>
                  <Link to="/instructor/courses">View All</Link>
                </Button>
              </div>
            </CardHeader>
            <CardContent>
              <DataTable
                data={recentCourses}
                columns={[
                  {
                    key: 'title',
                    header: 'Course',
                    render: (course) => (
                      <div>
                        <p className="font-medium">{course.title}</p>
                        <Badge variant={course.status === 'published' ? 'default' : 'secondary'} className="mt-1">
                          {course.status}
                        </Badge>
                      </div>
                    ),
                  },
                  {
                    key: 'students',
                    header: 'Students',
                    render: (course) => (
                      <span className="font-medium">{course.students}</span>
                    ),
                  },
                  {
                    key: 'revenue',
                    header: 'Revenue',
                    render: (course) => (
                      <span className="font-medium">${course.revenue.toLocaleString()}</span>
                    ),
                  },
                  {
                    key: 'actions',
                    header: 'Actions',
                    render: (course) => (
                      <div className="flex gap-2">
                        <Button size="sm" variant="ghost" asChild>
                          <Link to={`/instructor/courses/${course.id}`}>
                            <Eye className="h-4 w-4" />
                          </Link>
                        </Button>
                        <Button size="sm" variant="ghost" asChild>
                          <Link to={`/instructor/courses/${course.id}/edit`}>
                            <Edit className="h-4 w-4" />
                          </Link>
                        </Button>
                        <Button size="sm" variant="ghost" asChild>
                          <Link to={`/instructor/courses/${course.id}/analytics`}>
                            <BarChart3 className="h-4 w-4" />
                          </Link>
                        </Button>
                      </div>
                    ),
                  },
                ]}
                keyExtractor={(course) => course.id}
              />
            </CardContent>
          </Card>

          {/* Recent Students */}
          <Card className="mt-6">
            <CardHeader>
              <CardTitle>Recent Enrollments</CardTitle>
              <CardDescription>
                New students in your courses
              </CardDescription>
            </CardHeader>
            <CardContent>
              <DataTable
                data={recentStudents}
                columns={[
                  {
                    key: 'name',
                    header: 'Student',
                    render: (student) => (
                      <div>
                        <p className="font-medium">{student.name}</p>
                        <p className="text-sm text-muted-foreground">{student.email}</p>
                      </div>
                    ),
                  },
                  {
                    key: 'course',
                    header: 'Course',
                  },
                  {
                    key: 'progress',
                    header: 'Progress',
                    render: (student) => (
                      <div className="flex items-center gap-2">
                        <div className="h-2 w-24 rounded-full bg-muted">
                          <div
                            className="h-full rounded-full bg-primary"
                            style={{ width: `${student.progress}%` }}
                          />
                        </div>
                        <span className="text-sm">{student.progress}%</span>
                      </div>
                    ),
                  },
                ]}
                keyExtractor={(student) => student.id}
              />
            </CardContent>
          </Card>
        </div>

        {/* Sidebar */}
        <div className="space-y-6">
          {/* Pending Reviews */}
          <Card>
            <CardHeader>
              <CardTitle>Pending Reviews</CardTitle>
              <CardDescription>
                Assignments waiting for review
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              {pendingReviews.map((item) => (
                <div key={item.id} className="space-y-2 border-b pb-4 last:border-0 last:pb-0">
                  <div>
                    <p className="font-medium leading-none">{item.assignment}</p>
                    <p className="text-sm text-muted-foreground mt-1">
                      by {item.student}
                    </p>
                  </div>
                  <div className="flex items-center justify-between">
                    <p className="text-xs text-muted-foreground">
                      {item.submittedAt}
                    </p>
                    <Button size="sm" variant="outline" asChild>
                      <Link to={`/instructor/assignments/${item.id}`}>
                        Review
                      </Link>
                    </Button>
                  </div>
                </div>
              ))}

              {pendingReviews.length === 0 && (
                <p className="text-sm text-muted-foreground text-center py-4">
                  No pending reviews
                </p>
              )}
            </CardContent>
          </Card>

          {/* Quick Actions */}
          <Card>
            <CardHeader>
              <CardTitle>Quick Actions</CardTitle>
            </CardHeader>
            <CardContent className="space-y-2">
              <Button variant="outline" className="w-full justify-start" asChild>
                <Link to="/instructor/courses/new">
                  <Plus className="mr-2 h-4 w-4" />
                  Create New Course
                </Link>
              </Button>
              <Button variant="outline" className="w-full justify-start" asChild>
                <Link to="/instructor/students">
                  <Users className="mr-2 h-4 w-4" />
                  View All Students
                </Link>
              </Button>
              <Button variant="outline" className="w-full justify-start" asChild>
                <Link to="/instructor/analytics">
                  <BarChart3 className="mr-2 h-4 w-4" />
                  View Analytics
                </Link>
              </Button>
            </CardContent>
          </Card>

          {/* Performance Summary */}
          <Card>
            <CardHeader>
              <CardTitle>This Month</CardTitle>
              <CardDescription>
                Your performance overview
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="flex items-center justify-between">
                <span className="text-sm text-muted-foreground">New Enrollments</span>
                <span className="font-bold">+156</span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-muted-foreground">Completed Courses</span>
                <span className="font-bold">89</span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-muted-foreground">Avg. Completion Rate</span>
                <span className="font-bold">72%</span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-muted-foreground">Student Satisfaction</span>
                <span className="font-bold">4.7/5</span>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
};
import { useState } from 'react';
import { Link } from 'react-router-dom';
import { useCourses } from '@/hooks/useCourse';
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
  CardDescription,
} from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Badge } from '@/components/ui/Badge';
import { Select } from '@/components/ui/Select';
import { Pagination, PaginationInfo } from '@/components/ui/Pagination';
import { SkeletonCard } from '@/components/ui/Skeleton';
import { EmptyState } from '@/components/common/EmptyState';
import { SearchBar } from '@/components/common/Searchbar';
import { BookOpen, Clock, Star, Users, Grid, List } from 'lucide-react';
import { CourseLevel } from '@/types';
import { formatCurrency } from '@/utils/format';

// ✅ Main component
export const CoursesListPage = () => {
  // ---------- State ----------
  const [page, setPage] = useState(0);
  const [searchQuery, setSearchQuery] = useState('');
  const [levelFilter, setLevelFilter] = useState<CourseLevel | ''>('');
  const [viewMode, setViewMode] = useState<'grid' | 'list'>('grid');
  const pageSize = 12;

  // ---------- Data Fetching ----------
  const { data, isLoading, isError } = useCourses(page, pageSize, {
    search: searchQuery,
    level: levelFilter || undefined,
  });

  // ---------- Handlers ----------
  const handleSearch = (query: string) => {
    setSearchQuery(query);
    setPage(0);
  };

  const handleLevelFilter = (level: string) => {
    setLevelFilter(level as CourseLevel | '');
    setPage(0);
  };

  // ---------- Render ----------
  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-bold">Browse Courses</h1>
        <p className="text-muted-foreground mt-2">
          Discover courses and start learning today
        </p>
      </div>

      {/* Filters and Search */}
      <Card>
        <CardContent className="pt-6">
          <div className="flex flex-col gap-4 md:flex-row md:items-center">
            {/* Search */}
            <div className="flex-1">
              <SearchBar
                value={searchQuery}
                onChange={setSearchQuery}
                onSearch={handleSearch}
                placeholder="Search courses..."
              />
            </div>

            {/* Level Filter */}
            <Select
              value={levelFilter}
              onChange={(e) => handleLevelFilter(e.target.value)}
              options={[
                { value: '', label: 'All Levels' },
                { value: CourseLevel.BEGINNER, label: 'Beginner' },
                { value: CourseLevel.INTERMEDIATE, label: 'Intermediate' },
                { value: CourseLevel.ADVANCED, label: 'Advanced' },
                { value: CourseLevel.EXPERT, label: 'Expert' },
              ]}
              className="w-full md:w-48"
            />

            {/* View Mode Toggle */}
            <div className="flex gap-2">
              <Button
                variant={viewMode === 'grid' ? 'default' : 'outline'}
                size="icon"
                onClick={() => setViewMode('grid')}
              >
                <Grid className="h-4 w-4" />
              </Button>
              <Button
                variant={viewMode === 'list' ? 'default' : 'outline'}
                size="icon"
                onClick={() => setViewMode('list')}
              >
                <List className="h-4 w-4" />
              </Button>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Results Info */}
      {data && (
        <div className="flex items-center justify-between">
          <PaginationInfo
            currentPage={page}
            pageSize={pageSize}
            totalElements={data.totalElements}
          />
          <p className="text-sm text-muted-foreground">
            Showing {data.numberOfElements} of {data.totalElements} courses
          </p>
        </div>
      )}

      {/* Loading State */}
      {isLoading && (
        <div
          className={
            viewMode === 'grid'
              ? 'grid gap-6 md:grid-cols-2 lg:grid-cols-3'
              : 'space-y-4'
          }
        >
          {[...Array(6)].map((_, i) => (
            <SkeletonCard key={i} />
          ))}
        </div>
      )}

      {/* Error State */}
      {isError && (
        <EmptyState
          icon={BookOpen}
          title="Failed to load courses"
          description="There was an error loading the courses. Please try again."
        />
      )}

      {/* Empty State */}
      {data && data.content.length === 0 && !isLoading && (
        <EmptyState
          icon={BookOpen}
          title="No courses found"
          description="Try adjusting your search or filters to find what you're looking for."
        />
      )}

      {/* Courses Grid/List */}
      {!isLoading && data && data.content.length > 0 && (
        <div
          className={
            viewMode === 'grid'
              ? 'grid gap-6 md:grid-cols-2 lg:grid-cols-3'
              : 'space-y-4'
          }
        >
          {data.content.map((course: any) => (
            <Card
              key={course.id}
              className={
                viewMode === 'list'
                  ? 'flex items-center gap-4 p-4'
                  : 'overflow-hidden'
              }
            >
              {/* Thumbnail */}
              <div
                className={
                  viewMode === 'list'
                    ? 'w-32 h-24 rounded-lg bg-muted'
                    : 'h-40 w-full bg-muted'
                }
              >
                {course.thumbnail && (
                  <img
                    src={course.thumbnail}
                    alt={course.title}
                    className="object-cover w-full h-full rounded-lg"
                  />
                )}
              </div>

              {/* Course Info */}
              <div className={viewMode === 'list' ? 'flex-1' : 'p-4'}>
                <CardHeader className="p-0">
                  <CardTitle>
                    <Link
                      to={`/courses/${course.id}`}
                      className="hover:underline"
                    >
                      {course.title}
                    </Link>
                  </CardTitle>
                  <CardDescription>{course.shortDescription}</CardDescription>
                </CardHeader>

                <CardContent className="p-0 mt-2 flex flex-wrap gap-3 text-sm text-muted-foreground">
                  <div className="flex items-center gap-1">
                    <Clock className="h-4 w-4" /> {course.duration} hrs
                  </div>
                  <div className="flex items-center gap-1">
                    <Users className="h-4 w-4" /> {course.studentsEnrolled}{' '}
                    students
                  </div>
                  <div className="flex items-center gap-1">
                    <Star className="h-4 w-4" /> {course.rating}
                  </div>
                  <Badge variant="outline">{course.level}</Badge>
                </CardContent>

                <CardFooter className="p-0 mt-3 flex items-center justify-between">
                  <span className="font-semibold text-primary">
                    {formatCurrency(course.price)}
                  </span>
                  <Button asChild>
                    <Link to={`/courses/${course.id}`}>View Details</Link>
                  </Button>
                </CardFooter>
              </div>
            </Card>
          ))}
        </div>
      )}

      {/* Pagination */}
      {data && data.totalPages > 1 && (
        <div className="flex justify-center mt-8">
          <Pagination
            currentPage={page}
            totalPages={data.totalPages}
            onPageChange={setPage}
          />
        </div>
      )}
    </div>
  );
};

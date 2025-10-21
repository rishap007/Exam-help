// ==========================================
// MAIN HOMEPAGE COMPONENT
// Save as: src/pages/home/HomePage.tsx
// ==========================================

import { useState } from 'react';
import { BookOpen, Menu, X } from 'lucide-react';

// // Import all the home page sections
// import { HeroSection } from '@/components/home/HeroSection';
// import { TeacherHighlight } from '@/components/home/TeacherHighlight';
// import { CategoryGrid } from '@/components/home/CategoryGrid';
// import { AcademicExcellence } from '@/components/home/AcademicExcellence';
// import { Footer } from '@/components/home/Footer';
import { HeroSection } from '../../components/home/HeroSection';
import { TeacherHighlight } from '../../components/home/TeacherHighlight';
import { CategoryGrid } from '../../components/home/CategoryGrid';
import { AcademicExcellence } from '../../components/home/AcademicExcellence';
import { Footer } from '../../components/home/Footer';

export const HomePage = () => {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  return (
    <div className="min-h-screen bg-background">
      {/* Navbar */}
      <nav className="sticky top-0 z-50 border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
        <div className="container mx-auto px-4">
          <div className="flex h-16 items-center justify-between">
            {/* Logo */}
            <a href="/" className="flex items-center gap-2">
              <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-primary text-primary-foreground">
                <BookOpen className="h-6 w-6" />
              </div>
              <span className="text-xl font-bold">Class Topper</span>
            </a>

            {/* Desktop Navigation */}
            <div className="hidden items-center gap-6 md:flex">
              <a href="/courses" className="text-sm font-medium hover:text-primary transition-colors">
                All Courses
              </a>
              <a href="/bookstore" className="text-sm font-medium hover:text-primary transition-colors">
                Book Store
              </a>
              <a href="/mock-tests" className="text-sm font-medium hover:text-primary transition-colors">
                Mock Tests
              </a>
            </div>

            {/* Auth Buttons - Desktop */}
            <div className="hidden items-center gap-3 md:flex">
              <a
                href="/login"
                className="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 hover:bg-accent hover:text-accent-foreground h-10 px-4 py-2"
              >
                Login
              </a>
              <a
                href="/register"
                className="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 bg-primary text-primary-foreground hover:bg-primary/90 h-10 px-4 py-2"
              >
                Register
              </a>
            </div>

            {/* Mobile Menu Button */}
            <button
              onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
              className="md:hidden p-2"
            >
              {mobileMenuOpen ? <X className="h-6 w-6" /> : <Menu className="h-6 w-6" />}
            </button>
          </div>

          {/* Mobile Menu */}
          {mobileMenuOpen && (
            <div className="border-t py-4 md:hidden">
              <div className="flex flex-col gap-4">
                <a
                  href="/courses"
                  className="text-sm font-medium hover:text-primary"
                  onClick={() => setMobileMenuOpen(false)}
                >
                  All Courses
                </a>
                <a
                  href="/bookstore"
                  className="text-sm font-medium hover:text-primary"
                  onClick={() => setMobileMenuOpen(false)}
                >
                  Book Store
                </a>
                <a
                  href="/mock-tests"
                  className="text-sm font-medium hover:text-primary"
                  onClick={() => setMobileMenuOpen(false)}
                >
                  Mock Tests
                </a>
                <div className="flex flex-col gap-2 pt-4 border-t">
                  <a
                    href="/login"
                    className="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium transition-colors border border-input bg-background hover:bg-accent hover:text-accent-foreground h-10 px-4 py-2"
                  >
                    Login
                  </a>
                  <a
                    href="/register"
                    className="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium transition-colors bg-primary text-primary-foreground hover:bg-primary/90 h-10 px-4 py-2"
                  >
                    Register
                  </a>
                </div>
              </div>
            </div>
          )}
        </div>
      </nav>

      {/* Hero Section */}
      <HeroSection />

      {/* Teacher Highlight */}
      <TeacherHighlight />

      {/* Exam Categories */}
      <CategoryGrid />

      {/* Academic Excellence */}
      <AcademicExcellence />

      {/* Footer */}
      <Footer />
    </div>
  );
};

export default HomePage;

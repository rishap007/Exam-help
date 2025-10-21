// ==========================================
// CATEGORY GRID COMPONENT
// Save as: src/components/home/CategoryGrid.tsx
// ==========================================

import { ChevronRight } from 'lucide-react';
import { EXAM_CATEGORIES } from '@/config/homePageConfig';

export const CategoryGrid = () => {
  return (
    <section className="py-16 md:py-24">
      <div className="container mx-auto px-4">
        <div className="text-center mb-12">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">
            Choose Your Path
          </h2>
          <p className="text-muted-foreground max-w-2xl mx-auto">
            Select from our comprehensive range of courses designed for different competitive exams and board preparations
          </p>
        </div>

        <div className="grid sm:grid-cols-2 lg:grid-cols-4 gap-6 max-w-7xl mx-auto">
          {EXAM_CATEGORIES.map((category) => (
            <a
              key={category.id}
              href={category.link}
              className="group relative overflow-hidden rounded-xl bg-white border border-border hover:shadow-xl transition-all duration-300 hover:-translate-y-2"
            >
              <div className={`absolute inset-0 bg-gradient-to-br ${category.color} opacity-0 group-hover:opacity-10 transition-opacity`} />
              
              <div className="p-6">
                <div className="text-5xl mb-4">{category.icon}</div>
                <h3 className="text-xl font-bold mb-2">{category.title}</h3>
                <p className="text-sm text-muted-foreground mb-4">{category.description}</p>
                
                <div className="flex flex-wrap gap-2 mb-4">
                  {category.subjects.map((subject, idx) => (
                    <span
                      key={idx}
                      className="text-xs px-2 py-1 rounded-full bg-accent"
                    >
                      {subject}
                    </span>
                  ))}
                </div>

                <div className="flex items-center text-sm font-semibold text-primary group-hover:gap-2 transition-all">
                  Explore Courses
                  <ChevronRight className="h-4 w-4 group-hover:translate-x-1 transition-transform" />
                </div>
              </div>
            </a>
          ))}
        </div>
      </div>
    </section>
  );
};

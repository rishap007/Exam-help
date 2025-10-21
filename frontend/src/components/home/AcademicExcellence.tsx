// ==========================================
// ACADEMIC EXCELLENCE COMPONENT
// Save as: src/components/home/AcademicExcellence.tsx
// ==========================================

import { Star } from 'lucide-react';
import { SUCCESS_STORIES } from '@/config/homePageConfig';

export const AcademicExcellence = () => {
  return (
    <section className="py-16 md:py-24 bg-gradient-to-br from-primary/5 to-secondary/5">
      <div className="container mx-auto px-4">
        <div className="text-center mb-12">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">
            Academic Excellence
          </h2>
          <p className="text-muted-foreground">
            Our students' success stories inspire us every day
          </p>
        </div>

        <div className="grid md:grid-cols-3 gap-8 max-w-6xl mx-auto">
          {SUCCESS_STORIES.map((story) => (
            <div
              key={story.id}
              className="bg-white rounded-xl overflow-hidden shadow-lg hover:shadow-2xl transition-all duration-300 hover:-translate-y-2"
            >
              <div className="relative h-48 bg-gradient-to-br from-primary to-secondary">
                <img
                  src={story.image}
                  alt={story.studentName}
                  className="absolute bottom-0 left-1/2 -translate-x-1/2 translate-y-1/2 h-32 w-32 rounded-full border-4 border-white object-cover"
                />
              </div>
              
              <div className="pt-20 p-6 text-center">
                <div className="inline-flex items-center gap-1 bg-green-100 text-green-700 px-3 py-1 rounded-full text-sm font-semibold mb-3">
                  <Star className="h-4 w-4 fill-current" />
                  {story.score}
                </div>
                
                <h3 className="font-bold text-lg mb-1">{story.studentName}</h3>
                <p className="text-sm text-primary font-semibold mb-3">{story.achievement}</p>
                <p className="text-sm text-muted-foreground italic">
                  "{story.testimonial}"
                </p>
              </div>
            </div>
          ))}
        </div>

        <div className="text-center mt-12">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-8 max-w-4xl mx-auto">
            <div className="text-center">
              <div className="text-4xl font-bold text-primary mb-2">95%</div>
              <div className="text-sm text-muted-foreground">Success Rate</div>
            </div>
            <div className="text-center">
              <div className="text-4xl font-bold text-primary mb-2">50K+</div>
              <div className="text-sm text-muted-foreground">Students</div>
            </div>
            <div className="text-center">
              <div className="text-4xl font-bold text-primary mb-2">1000+</div>
              <div className="text-sm text-muted-foreground">Top Rankers</div>
            </div>
            <div className="text-center">
              <div className="text-4xl font-bold text-primary mb-2">4.9/5</div>
              <div className="text-sm text-muted-foreground">Rating</div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};
